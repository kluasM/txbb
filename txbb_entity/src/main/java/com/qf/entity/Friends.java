package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Friends implements Serializable {
    private int uid;
    private int fid;
    private Date ctime;
    private int status;
    @TableField(exist = false)
    private User friend;
}
