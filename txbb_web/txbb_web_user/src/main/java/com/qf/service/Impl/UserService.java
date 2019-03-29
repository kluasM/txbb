package com.qf.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.dao.UserDao;
import com.qf.entity.User;
import com.qf.entity.WsMsg;
import com.qf.feign.ChatFeign;
import com.qf.service.IUserService;
import com.qf.util.Md5Util;
import com.qf.util.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ChatFeign chatFeign;
    @Override
    public int register(User user) {
        System.out.println(user);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",user.getUsername());
        User uid = userDao.selectOne(queryWrapper);
        if (uid!=null){
            return -1;
        }
        user.setPassword(Md5Util.md5(user.getPassword()));
        //生成二维码 - fastdfs

        //生成一个临时文件保存二维码
        File file = null;
        try {
            file = File.createTempFile(user.getUsername()+"qrcode", ".png");

            //生成二维码
            boolean flag = QRCodeUtils.createQRCode(file, "txbb:" + user.getUsername());
            if(flag){
                //将二维码上传到fastdfs
                StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                        new FileInputStream(file),
                        file.length(),
                        "PNG",
                        null);

                user.setIdcard(storePath.getFullPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(file != null){
                file.delete();
            }
        }
        return userDao.insert(user);
    }

    @Override
    public User login(String username, String password, String uuid) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",username);
        queryWrapper.eq("password",Md5Util.md5(password));
        User user = userDao.selectOne(queryWrapper);
        if (user!=null){
            //当前登陆的设备号

            //将用户ID和设备号保存到redis中，绑定起来
            String oldId = (String) redisTemplate.opsForValue().get(user.getId());
            if (oldId!=null){
                //让oldId下线,消息发送给通信微服务
                WsMsg wsMsg=new WsMsg(-1,-1,100,oldId,null);
                chatFeign.sendMsg(wsMsg);
            }
            //将新用户的Id和设备Id进行绑定
            redisTemplate.opsForValue().set(user.getId(),uuid);
        }
        return user;
    }

    @Override
    public int updateHeader(String header, String headerCrm, Integer uid) {
        User user = userDao.selectById(uid);
        user.setHeader(header);
        user.setHeaderCrm(headerCrm);

        return userDao.updateById(user);
    }

    @Override
    public User searchByUserName(String username) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",username);
        return userDao.selectOne(queryWrapper);
    }

    @Override
    public User queryById(int id) {
        return userDao.selectById(id);
    }
}
