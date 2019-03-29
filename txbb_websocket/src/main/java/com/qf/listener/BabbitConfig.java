package com.qf.listener;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BabbitConfig {
    @Value("${ws.ip}")
    private String ip;
    @Value("${ws.port}")
    private String port;
    @Bean
    public Queue getQueue(){
        return new Queue("msg_queue_"+ip+":"+port);
    }

    @Bean
    public FanoutExchange getExchange(){
        return new FanoutExchange("msg_exchange");
    }
    /**
     * 绑定队列
     */
    @Bean
    public Binding binding(FanoutExchange fanoutExchange,Queue queue){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}
