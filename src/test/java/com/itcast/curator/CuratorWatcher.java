package com.itcast.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author zhanghao
 * @date 2020/6/9 - 9:37
 */
public class CuratorWatcher {

    String IP = "192.168.44.139:2181,192.168.44.139:2182,192.168.44.139:2183";
    CuratorFramework client;

    @Before
    public void before(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3 );
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    @After
    public void after(){
        client.close();
    }

    @Test
    public void watcher1()throws Exception {
        // 监听某个节点的数据变化
        //arg1:连接对象 arg2:监听的节点路径
        final NodeCache nodeCache = new NodeCache(client, "/watcher1");
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            // 节点变化回调的方法
            @Override
            public void nodeChanged() throws Exception {
                System.out.println(nodeCache.getCurrentData().getPath());
                System.out.println(new String(nodeCache.getCurrentData().getData()));
            }
        });
        try { TimeUnit.SECONDS.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("end");
        nodeCache.close();
    }

    @Test
    public void watcher2()throws Exception {
        // 监视子节点的变化
        //arg1:连接对象 arg2:监视的节点路径 arg3:事件中是否可以获取节点的数据
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/watcher1", true);
        pathChildrenCache.start();

        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            // 当子节点发声变化时回调的方法
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println(event.getType());
                System.out.println(event.getData().getPath());
                System.out.println(new String(event.getData().getData()));
            }
        });

        try { TimeUnit.SECONDS.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("end");
        pathChildrenCache.close();

    }
}
