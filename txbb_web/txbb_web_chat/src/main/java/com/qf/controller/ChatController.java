package com.qf.controller;

import com.qf.entity.WsMsg;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("/send")
    public void sendMsg(@RequestBody WsMsg wsMsg){
        System.out.println("wsMsg---->"+wsMsg);
        //将该消息通过RabbitMQ发送给交换机并通知给Netty集群
        if (wsMsg.getCid()==null){
            //找到设备ID
            String cid= (String) redisTemplate.opsForValue().get(wsMsg.getToid());
            wsMsg.setCid(cid);
        }
        rabbitTemplate.convertAndSend("msg_exchange",null,wsMsg);
    }
}
