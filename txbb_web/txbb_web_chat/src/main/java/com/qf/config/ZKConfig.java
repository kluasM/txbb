package com.qf.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZKConfig {
    @Value("${zk.host}")
    private String ZKHost;
    @Bean
    public ZooKeeper creatZookeeper(){
        ZooKeeper zooKeeper=null;
        try {
            zooKeeper=new ZooKeeper(ZKHost, 60, new Watcher() {
                @Override
                public void process(WatchedEvent event) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return zooKeeper;
    }
}
