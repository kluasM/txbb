package com.qf.websocket;

import com.qf.websocket.handler.ConnWebSocketHandler;
import com.qf.websocket.handler.HeartWebSocketHandler;
import com.qf.websocket.handler.TextWebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class WebSocketServer implements CommandLineRunner{

    private static final EventLoopGroup masterGroup=new NioEventLoopGroup();
    private static final EventLoopGroup slaveGroup=new NioEventLoopGroup();
    @Autowired
    private TextWebSocketHandler textWebSocketHandler;
    @Autowired
    private ConnWebSocketHandler connWebSocketHandler;
    @Autowired
    private HeartWebSocketHandler heartWebSocketHandler;
    @Value("${ws.port}")
    private int port;
    @Value("${zk.host}")
    private String zkHost;
    @Value("${ws.ip}")
    private String ip;
    private Channel channel;


    private ChannelFuture init(){
        System.out.println("Handler初始化！");
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        serverBootstrap
                .group(masterGroup,slaveGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline=channel.pipeline();
                        //编解码器
                        //因为websocket会先发送Http协议，所以需要添加一个Http协议的编解码器
                        pipeline.addLast(new HttpServerCodec());
                        //聚合Http协议，通过这个编解码器之后的Handler接收到的数据都是FullHttpRequest
                        pipeline.addLast(new HttpObjectAggregator(512*1024));
                        //该编解码器用于处理websocket的升级握手，并且还能帮助我们进行非数据帧的处理(Ping、Pong、Close)
                        pipeline.addLast(new WebSocketServerProtocolHandler("/txbb"));
                        //添加一个读超时的处理器
                        pipeline.addLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS));
                        pipeline.addLast(textWebSocketHandler);//验证消息格式
                        pipeline.addLast(connWebSocketHandler);//处理连接握手
                        pipeline.addLast(heartWebSocketHandler);//处理心跳消息

                    }
                });
        System.out.println("初始化结束!");
        ChannelFuture channelFuture = serverBootstrap.bind(port);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    System.out.println("webSocket端口已经启动，端口为:"+port);
                    channel = channelFuture.channel();
                    //----注册zookeeper
                    //连接zookeeper
                   try {
                       ZooKeeper zooKeeper = new ZooKeeper(zkHost, 60, new Watcher() {
                           @Override
                           public void process(WatchedEvent event) {
                           }
                       });
                       //在根节点下创建一个Netty节点
                       Stat stat=zooKeeper.exists("/netty",null);
                       if (stat==null){
                           zooKeeper.create(
                                   "/netty",
                                   null,
                                   ZooDefs.Ids.OPEN_ACL_UNSAFE,
                                   CreateMode.PERSISTENT
                           );
                       }
                       //节点的值
                       String value=ip+":"+port;
                        //创建当前服务器所对应的临时节点
                       String tempNode = zooKeeper.create(
                               "/netty/server",
                               value.getBytes("utf-8"),
                               ZooDefs.Ids.OPEN_ACL_UNSAFE,
                               CreateMode.EPHEMERAL_SEQUENTIAL
                       );
                       System.out.println("临时节点--->"+tempNode);
                   }catch (Exception e){
                       e.printStackTrace();
                       destory();
                   }
                }else {
                    //绑定端口失败
                    destory();
                }
            }
        });
        return channelFuture;
    }

    private void destory(){
        if (channel !=null && channel.isActive()){
            channel.close();
        }
        masterGroup.shutdownGracefully();
        slaveGroup.shutdownGracefully();
    }
    @Override
    public void run(String... args) throws Exception {
        ChannelFuture init = init();

        //设置一个运行时的销毁回调
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                destory();
            }
        });
        //同步阻塞
        init.channel().closeFuture().syncUninterruptibly();
    }
}
