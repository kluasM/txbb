package com.qf.controller;

import com.qf.entity.ResultData;
import com.qf.util.Constant;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/netty")
public class NettyController {
    @Autowired
    private ZooKeeper zooKeeper;

    private AtomicInteger index=new AtomicInteger(0);

    @RequestMapping("/getServer")
    public ResultData<String> getNettyServer() throws UnsupportedEncodingException {
        try {
            List<String> zooKeeperChildren = zooKeeper.getChildren("/netty", null);
            if (zooKeeperChildren!=null && zooKeeperChildren.size()>0){
                //从这些子节点中选择一个节点返回给浏览器，让浏览器连接服务器
                String balance = loadBalance(zooKeeperChildren);
                byte[] data = zooKeeper.getData("/netty/"+balance, null, null);
                String ip=new String(data,"utf-8");
                return ResultData.createSuccResultData(ip);
            }
            return ResultData.createSuccResultData("");
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResultData.createErrorResultData(Constant.ERROR_CODE,"服务地址获取异常");
    }

    public String loadBalance(List<String> stringList){
        int i=index.getAndIncrement() % stringList.size();
        System.out.println("i--->"+i);
        return stringList.get(i);
    }

}
