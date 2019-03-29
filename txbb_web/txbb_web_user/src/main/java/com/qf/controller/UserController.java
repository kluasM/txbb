package com.qf.controller;

import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.IUserService;
import com.qf.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;
    @Value("${fdfs.serverip}")
    private String fdfsip;
    @RequestMapping("/register")
    public ResultData register(User user){
        int register = userService.register(user);
        log.info("有用户注册----->:"+register);
        ResultData resultData=new ResultData();
        if (register>0) {
            resultData.setCode(Constant.SUCC_CODE);
            resultData.setMsg("succ");
        }else if (register == -1){
            resultData.setCode(Constant.USERNAME_HAVE_CODE);
            resultData.setMsg("用户名存在!");
        }else {
            resultData.setCode(Constant.ERROR_CODE);
            resultData.setMsg("其他错误!");
        }
        return resultData;
    }
    @RequestMapping("/login")
    public ResultData<User> login(String username,String password,String uuid){
        User user = userService.login(username, password, uuid);
        if (user!=null){
            user.setHeaderCrm(fdfsip+"/"+user.getHeaderCrm());
            user.setHeader(fdfsip+"/"+user.getHeader());
            user.setIdcard(fdfsip+"/"+user.getIdcard());
            System.out.println("user数据---->"+user);
            return ResultData.createSuccResultData(user);
        }else{
            return ResultData.createErrorResultData(Constant.USERNAME_PASSWORD_ERROR_CODE,"用户名或密码错误!");
        }
    }
    @RequestMapping("/updateHeader")
    public ResultData<Boolean> updateUserHeader(String header,String headerCrm,Integer uid){
        int result=userService.updateHeader(header,headerCrm,uid);
        if (result>0){
            return ResultData.createSuccResultData(true);
        }
        return ResultData.createErrorResultData(Constant.ERROR_CODE,"图片修改失败");
    }
    @RequestMapping("/searchbyusername")
    public ResultData<User> serchFriendsByUserName(String username){
        User user = userService.searchByUserName(username);
        user.setHeader(fdfsip+"/"+user.getHeader());
        user.setHeaderCrm(fdfsip+"/"+user.getHeaderCrm());
        return ResultData.createSuccResultData(user);
    }
    @RequestMapping("/get")
    public User queryById(int id){
        User user = userService.queryById(id);
        user.setHeader(fdfsip + "/" + user.getHeader());
        user.setHeaderCrm(fdfsip + "/" + user.getHeaderCrm());
        user.setIdcard(fdfsip + "/" + user.getIdcard());
        return user;
    }
}
