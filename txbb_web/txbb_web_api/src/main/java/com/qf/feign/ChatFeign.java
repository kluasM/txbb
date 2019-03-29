package com.qf.feign;

import com.qf.entity.WsMsg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("web-chat")
public interface ChatFeign {
    @RequestMapping("/chat/send")
    void sendMsg(@RequestBody WsMsg wsMsg);
}
