package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IFriendDao;
import com.qf.entity.Friends;
import com.qf.service.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FriendService implements IFriendService {
    @Autowired
    private IFriendDao friendDao;
    @Override
    public boolean isFriend(int uid, int fid) {
        QueryWrapper<Friends> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        queryWrapper.eq("fid",fid);
        queryWrapper.ne("status",2);
        Integer count = friendDao.selectCount(queryWrapper);
        System.out.println("isFriend?--->"+count);
        return count>0? true:false;
    }

    @Override
    public int insertFriends(int uid, int fid) {
        Friends friends=new Friends();
        friends.setUid(uid);
        friends.setFid(fid);
        friends.setCtime(new Date());
        friends.setStatus(0);
        if (!isFriend(uid,fid)){
            friendDao.insert(friends);
        }
        if (!isFriend(fid, uid)){
            friends.setUid(fid);
            friends.setFid(uid);
            friendDao.insert(friends);
        }
        return 1;
    }

}
