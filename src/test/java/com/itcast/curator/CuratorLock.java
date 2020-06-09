package com.itcast.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author zhanghao
 * @date 2020/6/9 - 9:37
 */
public class CuratorLock {

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
    public void lock1()throws Exception {
        // 排它锁
        // arg1:连接对象 arg2:节点路径
        InterProcessLock interProcessLock = new InterProcessMutex(client, "/lock1");
        System.out.println("等待获取锁对象");
        interProcessLock.acquire();
        for (int i = 0; i < 10; i++) {
            try { TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(i);
        }
        interProcessLock.release();
        System.out.println("等待释放锁");
    }

    @Test
    public void lock2()throws Exception {
        // 读写锁
        InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(client, "/lock1");
        // 获取读锁对象
        InterProcessMutex interProcessLock = interProcessReadWriteLock.readLock();
        System.out.println("等待获取锁对象");
        interProcessLock.acquire();
        for (int i = 0; i < 10; i++) {
            try { TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(i);
        }
        interProcessLock.release();
        System.out.println("等待释放锁");
    }

    @Test
    public void lock3()throws Exception {
        // 读写锁
        InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(client, "/lock1");
        // 获取写锁对象
        InterProcessMutex interProcessLock = interProcessReadWriteLock.writeLock();
        System.out.println("等待获取锁对象");
        interProcessLock.acquire();
        for (int i = 0; i < 10; i++) {
            try { TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(i);
        }
        interProcessLock.release();
        System.out.println("等待释放锁");
    }
}
