package com.qf.service;

import com.qf.entity.FriendRequest;
import com.qf.entity.Friends;

import java.util.List;

public interface IFriendRequestService {
    int insertFriendRequest(FriendRequest friendRequest);
    List<FriendRequest> queryFriendRequest(int fromid);
    int friendRequestHandler(int rid,int status);
    List<Friends> listByUid(int uid);
}
