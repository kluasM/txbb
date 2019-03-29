package com.qf.service;

public interface IFriendService {
    boolean isFriend(int uid,int fid);
    int insertFriends(int uid,int fid);
}
