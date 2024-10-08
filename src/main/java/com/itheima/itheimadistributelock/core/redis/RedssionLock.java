package com.itheima.itheimadistributelock.core.redis;

import org.redisson.Redisson;
import org.redisson.RedissonFairLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class RedssionLock {
	//zookeeper 分布锁
	public static RLock getLock() {
		Config config = new Config();
		//指定使用单节点部署方式
		config.useSingleServer().setAddress("redis://192.168.93.132:6379");//.setPassword("mkxiaoer");
//		config.useSingleServer().setPassword("root");
		config.useSingleServer().setConnectionPoolSize(500);//设置对于master节点的连接池中连接数最大为500
		config.useSingleServer().setIdleConnectionTimeout(10000);//如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
		config.useSingleServer().setConnectTimeout(30000);//同任何节点建立连接时的等待超时。时间单位是毫秒。
		config.useSingleServer().setTimeout(3000);//等待节点回复命令的时间。该时间从命令发送成功时开始计时。
		config.useSingleServer().setPingTimeout(30000);
		//获取RedissonClient对象
		RedissonClient redisson = Redisson.create(config);
		//获取锁对象
		RLock rLock = redisson.getLock("lock.lock");
//		redisson.getFairLock("");
		return rLock;
	}

	/**
	 * 演示可重入锁
	 * @param args
	 */
	public static void main(String[] args) {
		RLock lock = RedssionLock.getLock();
		lock.lock();
//		System.out.println("1");
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		lock.lock();
//		System.out.println("2");
//		// 整个方法解锁
//		lock.unlock();
		lock.unlock();
	}
}
