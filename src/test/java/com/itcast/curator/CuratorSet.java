package com.itcast.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanghao
 * @date 2020/6/9 - 9:37
 */
public class CuratorSet {

    String IP = "192.168.44.139:2181,192.168.44.139:2182,192.168.44.139:2183";
    CuratorFramework client;

    @Before
    public void before(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3 );
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("set")
                .build();
        client.start();
    }

    @After
    public void after(){
        client.close();
    }

    @Test
    public void set1()throws Exception {
        // 更新节点
        client.setData()
                .forPath("/node1","node12".getBytes());
    }

    @Test
    public void set2()throws Exception {
        client.setData()
                .withVersion(-1)
                .forPath("/node1","node1".getBytes());
        System.out.println("end");
    }

    @Test
    public void set3()throws Exception {
        // 异步方式修改节点数据
        client.setData()
                .withVersion(-1)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println(event.getPath());
                        System.out.println(event.getType());
                    }
                }).forPath("/node1","node12".getBytes());
        try { TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("end");
    }
}
