package com.qf.websocket.handler;

import com.qf.entity.WsMsg;
import com.qf.websocket.group.ChannelGroupUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理握手连接
 */
@Component
@ChannelHandler.Sharable
public class ConnWebSocketHandler extends SimpleChannelInboundHandler<WsMsg>{
    @Autowired
    private ChannelGroupUtil channelGroupUtil;


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有一个客户端上线了!");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有一个客户端下线了!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        channelGroupUtil.removeByChannel(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WsMsg wsMsg) throws Exception {
        if (wsMsg.getType()==1){
            //握手请求

            //获得当前的Channel对象，并且和设备对象进行统一管理
            Channel channel = channelHandlerContext.channel();
            //获得连接的设备号
            String cid = wsMsg.getCid();
            //放入集合中统一管理
            channelGroupUtil.put(cid, channel);
            System.out.println("有一个连接握手成功--->"+wsMsg+"当前管理的连接数量--->"+channelGroupUtil.size());
        }else {
            //非握手请求，继续往后透传
            channelHandlerContext.fireChannelRead(wsMsg);
        }
    }
}
