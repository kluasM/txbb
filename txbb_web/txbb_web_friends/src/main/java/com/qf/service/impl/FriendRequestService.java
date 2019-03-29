package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IFriendDao;
import com.qf.dao.IFriendRequest;
import com.qf.entity.FriendRequest;
import com.qf.entity.Friends;
import com.qf.entity.User;
import com.qf.entity.WsMsg;
import com.qf.feign.ChatFeign;
import com.qf.feign.UserFeign;
import com.qf.service.IFriendRequestService;
import com.qf.service.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FriendRequestService implements IFriendRequestService {
    @Autowired
    private IFriendRequest friendRequestDao;
    @Autowired
    private IFriendDao friendDao;
    @Autowired
    private IFriendService friendService;
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private ChatFeign chatFeign;
    @Override
    public int insertFriendRequest(FriendRequest friendRequest) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("fromid",friendRequest.getFromid());
        queryWrapper.eq("toid",friendRequest.getToid());
        Integer count = friendRequestDao.selectCount(queryWrapper);
        if (count>0){
            return -1;//申请已发送，待审核!
        }
        if (friendService.isFriend(friendRequest.getFromid(),friendRequest.getToid())){
            return -2;//isFriends!
        }
        //通知friendRequest.getToId(被申请者)有人给他发起了一个好友添加申请
        int toid=friendRequest.getToid();
        WsMsg wsMsg=new WsMsg(friendRequest.getFromid(),toid,101,null,null);
        chatFeign.sendMsg(wsMsg);
        return friendRequestDao.insert(friendRequest);
    }

    @Override
    public List<FriendRequest> queryFriendRequest(int fromid) {
        QueryWrapper<FriendRequest> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("fromid",fromid);
        queryWrapper.orderByDesc("stime");
        List<FriendRequest> friendRequestList = friendRequestDao.selectList(queryWrapper);
        for (FriendRequest friendRequest: friendRequestList) {
            User user = userFeign.queryById(friendRequest.getFromid());
            friendRequest.setFromUser(user);
        }
        return friendRequestList;
    }

    @Override
    public int friendRequestHandler(int rid, int status) {
        FriendRequest friendRequest = friendRequestDao.selectById(rid);
        friendRequest.setStatus(status);
        System.out.println("friendRequestStatus----->"+friendRequest);
        int result = friendRequestDao.updateById(friendRequest);
        if (result>0 && status==1){
            friendService.insertFriends(friendRequest.getFromid(),friendRequest.getToid());
        }
        return 1;
    }

    @Override
    public List<Friends> listByUid(int uid) {

        QueryWrapper<Friends> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        queryWrapper.ne("status",2);
        List<Friends> friendsList = friendDao.selectList(queryWrapper);
        for (Friends friends: friendsList) {
            User user = userFeign.queryById(friends.getFid());
            friends.setFriend(user);
        }
        return friendsList;
    }
}
