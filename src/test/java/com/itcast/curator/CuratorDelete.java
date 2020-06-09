package com.itcast.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author zhanghao
 * @date 2020/6/9 - 9:37
 */
public class CuratorDelete {

    String IP = "192.168.44.139:2181,192.168.44.139:2182,192.168.44.139:2183";
    CuratorFramework client;

    @Before
    public void before(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3 );
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("delete")
                .build();
        client.start();
    }

    @After
    public void after(){
        client.close();
    }

    @Test
    public void delete1()throws Exception {
        // 删除节点
        client.delete()
                .forPath("/node1");
        System.out.println("end");
    }

    @Test
    public void delete2()throws Exception {
        client.delete()
                .withVersion(0)
                .forPath("/node1");
        System.out.println("end");
    }

    @Test
    public void delete3()throws Exception {
        client.delete()
                .deletingChildrenIfNeeded()
                .withVersion(-1)
                .forPath("/node1");
        System.out.println("end");
    }

    @Test
    public void delete4()throws Exception {
        // 异步方式删除节点
        client.delete()
                .deletingChildrenIfNeeded()
                .withVersion(-1)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println(event.getType());
                        System.out.println(event.getPath());
                    }
                }).forPath("/node1");
        try { TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("end");
    }
}
