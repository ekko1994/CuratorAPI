package com.itcast.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanghao
 * @date 2020/6/9 - 9:37
 */
public class CuratorExists {

    String IP = "192.168.44.139:2181,192.168.44.139:2182,192.168.44.139:2183";
    CuratorFramework client;

    @Before
    public void before(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3 );
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("get")
                .build();
        client.start();
    }

    @After
    public void after(){
        client.close();
    }

    @Test
    public void exists1()throws Exception {
        Stat stat = client.checkExists()
                .forPath("/node1");
        System.out.println(stat);
    }

    @Test
    public void exists2()throws Exception {
        // 异步方式判断节点是否存在
        client.checkExists()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println(event.getType());
                        System.out.println(event.getPath());
                        System.out.println(event.getStat());
                    }
                }).forPath("/node2");
        try { TimeUnit.SECONDS.sleep(4);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("end");
    }
}
