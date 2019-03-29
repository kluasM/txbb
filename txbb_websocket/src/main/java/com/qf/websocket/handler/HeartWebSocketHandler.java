package com.qf.websocket.handler;

import com.qf.entity.WsMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 * 心跳消息Handler
 */
@Component
@ChannelHandler.Sharable
public class HeartWebSocketHandler extends SimpleChannelInboundHandler<WsMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WsMsg wsMsg) throws Exception {
        if (wsMsg.getType()==2){
            //是一个心跳消息
        }else{
            channelHandlerContext.fireChannelRead(wsMsg);
        }
    }
}
