package com.qf.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class User implements Serializable{
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String header;
    private String headerCrm;
    private String pinyin;
    private int status;
    private String idcard;
}
