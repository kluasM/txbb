package com.qf.feign;

import com.qf.entity.ResultData;
import com.qf.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "WEB-USER")
public interface UserFeign {
    @RequestMapping("/user/updateHeader")
    ResultData<Boolean> updateUserHeader(@RequestParam("header") String header,
                                         @RequestParam("headerCrm") String headerCrm,
                                         @RequestParam("uid") Integer uid);

    @RequestMapping("/user/get")
    User queryById(@RequestParam("id") int id);

}
