package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FriendRequest implements Serializable {
    private int id;
    private int fromid;//申请者
    private int toid;//被申请者
    private Date stime;
    private String content;
    private int status;
    @TableField(exist = false)
    private User fromUser;
}
