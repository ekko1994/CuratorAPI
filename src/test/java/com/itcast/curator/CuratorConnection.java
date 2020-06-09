package com.itcast.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryOneTime;

/**
 * @author zhanghao
 * @date 2020/6/9 - 9:21
 */
public class CuratorConnection {
    public static void main(String[] args) {
        // 创建连接对象
        CuratorFramework client = CuratorFrameworkFactory.builder()
                // ip地址端口号
                .connectString("192.168.44.139:2181,192.168.44.139:2182,192.168.44.139:2183")
                // 会话超时时间
                .sessionTimeoutMs(5000)
                // 重连机制
                .retryPolicy(new RetryOneTime(3000))
                // 命名空间
                .namespace("create")
                // 构建连接对象
                .build();
        // 打开链接
        client.start();
        System.out.println(client.isStarted());
        // 关闭连接
        client.close();
    }
}
