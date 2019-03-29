package com.qf.service;

import com.qf.entity.User;

public interface IUserService {
    int register(User user);
    User login(String username,String password,String uuid);
    int updateHeader(String header,String headerCrm,Integer uid);
    User searchByUserName(String username);
    User queryById(int id);
}
