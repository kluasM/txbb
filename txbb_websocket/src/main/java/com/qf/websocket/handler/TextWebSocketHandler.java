package com.qf.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.qf.entity.WsMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class TextWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //获得连接对象
        Channel channel=channelHandlerContext.channel();
       String msg=textWebSocketFrame.text();
        System.out.println("---------------->" + msg);
        WsMsg wsMsg =null;
        try{
            wsMsg = JSON.parseObject(msg, WsMsg.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (wsMsg!=null&&wsMsg.getType()>0){
            //格式符合协议规则
            //将该数据透传给下一个ChannelHandler处理
            channelHandlerContext.fireChannelRead(wsMsg);
        }else {
            System.out.println("客户端数据格式异常");
            channel.close();
        }
    }
}
