package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsMsg<T> implements Serializable {
    private int fromid;
    private int toid;
    /**
     * 1-连接请求
     * 2-心跳消息
     * 服务器发送给客户端的消息
     * 100-强制设备下线
     */
    private int type;
    private String cid;
    private T content;
}
