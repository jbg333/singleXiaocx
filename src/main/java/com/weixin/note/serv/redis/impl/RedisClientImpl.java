package com.weixin.note.serv.redis.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.weixin.redis.RedisClient;
import com.weixin.redis.lock.RedisLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 就一个小Demo 随便写下
 * 
 * @author bigsea
 * 
 */
public class RedisClientImpl extends AbstractRedisClientImpl implements RedisClient{
	protected final static Logger logger = LoggerFactory.getLogger(RedisClientImpl.class);

	protected JedisPool jedisPool;
	
    public JedisPool getJedisPool() {  
        return jedisPool;  
    }  
  
	public void setJedisPool(JedisPool pool) {
		jedisPool = pool;
	}
	

    
	public Jedis getJedis() {
		int timeoutCount = 0;
		while (true) {
			try {
				if (null != jedisPool) {
					logger.info("jedisInfo ... NumActive=" + jedisPool.getNumActive() + ", NumIdle=" + jedisPool.getNumIdle()
					+ ", NumWaiters=" + jedisPool.getNumWaiters() + ", isClosed=" + jedisPool.isClosed());
					return jedisPool.getResource();
				}
			} catch (Exception e) {
				if (e instanceof JedisConnectionException) {
					timeoutCount++;
					logger.error("getJedis timeoutCount={}", timeoutCount);
					if (timeoutCount > 3) {
						break;
					}
				} else {
					logger.error("jedisInfo ... NumActive=" + jedisPool.getNumActive() + ", NumIdle=" + jedisPool.getNumIdle()
							+ ", NumWaiters=" + jedisPool.getNumWaiters() + ", isClosed=" + jedisPool.isClosed());
					logger.error("GetJedis error,", e);
					break;
				}
			}
			break;
		}
		return null;

		
	}

	public void closeRedis(Jedis jedis) {
		try {
			jedis.close();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	
	public <T> long rpushByLock(String key, List<T> list) {
		long count = 0;
		if (list == null || list.size() <= 0) {
			return 0;
		}
		String[] jsons = new String[list.size()];
		int i = 0;
		for (T c : list) {
			final String json = JSON.toJSONString(c);
			jsons[i++] = json;
		}
		
		RedisLock redisLock = new RedisLock(this);
		// 增加锁
		if (!redisLock.lock(key)) {
			return count;
		}
		try {
			del(key);
			count = rpush(key, jsons);
		} finally {
			redisLock.unlock(key);
		}
		return count;

	}




	public <T> long lpush(String key, List<T> list) {
		if (list == null || list.size() <= 0) {
			return 0;
		}

		String[] jsons = new String[list.size()];
		int i = 0;
		for (T c : list) {
			final String json = JSON.toJSONString(c);
			jsons[i++] = json;
		}
		
		
		RedisLock redisLock = new RedisLock(this);
		// 增加锁
		if (!redisLock.lock(key)) {
			return 0;
		}
		try {
			return lpush(key, jsons);
		} finally {
			redisLock.unlock(key);
		}		
	}
	
	
	
	public <T> long rpush(String key, List<T> list) {
		if (list == null || list.size() <= 0) {
			return 0;
		}

		String[] jsons = new String[list.size()];
		int i = 0;
		for (T c : list) {
			final String json = JSON.toJSONString(c);
			jsons[i++] = json;
		}
		
		RedisLock redisLock = new RedisLock(this);
		// 增加锁
		if (!redisLock.lock(key)) {
			return 0;
		}		

		try{
			long count = rpush(key, jsons);
			return count;
		}finally{
			redisLock.unlock(key);
		}
	}
	

 

 



	 

	 

}
