package com.qf.websocket.group;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChannelGroupUtil {
    /**
     * 线程安全的Map
     */
    private Map<String,Channel> channelMap=new ConcurrentHashMap<>();

    /**
     * 管理设备号和channel对象
     * @param cid
     * @param channel
     * @return
     */
    public Channel put(String cid,Channel channel){
        return channelMap.put(cid,channel);
    }

    /**
     * 判断当前管理的链接长度
     * @return
     */
    public int size(){
        return channelMap.size();
    }

    /**
     * 根据设备号查询Channel对象
     * @param cid
     * @return
     */
    public Channel getCid(String cid){
        return channelMap.get(cid);
    }
    /**
     * 根据设备ID移除Channel
     * @param cid
     * @return
     */
    public Channel removeByCid(String cid){
        return channelMap.remove(cid);
    }

    public boolean removeByChannel(Channel channel){
        Set<Map.Entry<String,Channel>> entries=channelMap.entrySet();
        for (Map.Entry<String,Channel> entry:new HashSet<>(entries)) {
            if (entry.getValue()==channel){
                return entries.remove(entry.getKey());
            }
        }
        return false;
    }
}
