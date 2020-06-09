package com.itcast.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author zhanghao
 * @date 2020/6/9 - 9:37
 */
public class CuratorTransaction {

    String IP = "192.168.44.139:2181,192.168.44.139:2182,192.168.44.139:2183";
    CuratorFramework client;

    @Before
    public void before(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3 );
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("create")
                .build();
        client.start();
    }

    @After
    public void after(){
        client.close();
    }

    @Test
    public void ts1()throws Exception {
        // 第一句会成功
        client.create().forPath("/node1","node1".getBytes());
        client.setData().forPath("/node2","node2".getBytes());
    }

    @Test
    public void ts2()throws Exception {
        // 全部失败
        client.inTransaction()
                .create().forPath("/node1","node1".getBytes())
                .and()
                .create().forPath("/node2","node2".getBytes())
                .and()
                .commit();
    }
}
