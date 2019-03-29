package com.qf.controller;

import com.qf.entity.FriendRequest;
import com.qf.entity.Friends;
import com.qf.entity.ResultData;
import com.qf.service.IFriendRequestService;
import com.qf.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendsController {
    @Autowired
    private IFriendRequestService friendRequestService;
    @RequestMapping("/insertFriendRequest")
    public ResultData<Boolean> insertFriendRequest(FriendRequest friendRequest){
        int result = friendRequestService.insertFriendRequest(friendRequest);
        if (result>0){
            //添加好友申请成功
            return ResultData.createSuccResultData(true);
        }else if (result == -1){
            return ResultData.createErrorResultData(Constant.ERROR_CODE,"请不要重复添加");
        }else if (result == -2){
            return ResultData.createErrorResultData(Constant.ERROR_CODE,"你们已经是好友了!");
        }
            return ResultData.createErrorResultData(Constant.ERROR_CODE,"服务器异常");
    }

    @RequestMapping("/queryFriendRequest")
    public ResultData<List<FriendRequest>> queryFriendRequest(int fromid){
        List<FriendRequest> requestList = friendRequestService.queryFriendRequest(fromid);
        if (requestList.size()>0 && requestList !=null){
            return ResultData.createSuccResultData(requestList);
        }
        return ResultData.createErrorResultData(Constant.ERROR_CODE,"没有任何申请");
    }
    @RequestMapping("/friendRequestHandler")
    public ResultData<Boolean> friendRequestHandler(int rid,int status){
        System.out.println("status---->"+status);
        int result = friendRequestService.friendRequestHandler(rid, status);
        return ResultData.createSuccResultData(true);
    }
    @RequestMapping("/listByUid")
    public ResultData<List<Friends>> listByUserId(int uid){
        List<Friends> friends = friendRequestService.listByUid(uid);
        System.out.println("好友列表---->"+friends);
        return ResultData.createSuccResultData(friends);
    }
}
