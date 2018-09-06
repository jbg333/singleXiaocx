package com.weixin.note.serv.sso.cache.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.weixin.note.serv.redis.JedisProperties;
import com.weixin.note.serv.redis.RedisClient;
import com.weixin.note.serv.redis.impl.RedisClientImpl;
import com.weixin.note.serv.redis.impl.RedisSentinelClientImpl;
import com.weixin.note.serv.sso.SSOCache;
import com.weixin.note.serv.sso.token.SSOToken;

/**
 * 增加缓存
 * 
 * @author admin
 *
 */
public class SSORedis implements SSOCache {

	protected final static Logger logger = LoggerFactory.getLogger(SSORedis.class);

	// @Autowired
	RedisClient redisClient;

	public SSORedis() {
		this.redisClient = initRedis();

	}
	
	public static void main(String[] arg){
		SSORedis redis = new SSORedis();
		
		//redis.redisClient.set("aaa:aaa", "myname");
		
	}

	public synchronized RedisClient initRedis() {
		if (redisClient == null) {
			JedisProperties jedisProperties = RedisUtil.getJedisSentinelProperties();
			if (StringUtils.isNotEmpty(jedisProperties.getMasterName())){
				RedisSentinelClientImpl rc = new RedisSentinelClientImpl();
				rc.setJedisPool(rc.createJedisSentinelPool(jedisProperties));
				return rc;
			}else{
				jedisProperties = RedisUtil.getJedisProperties();
				RedisClientImpl rc = new RedisClientImpl();
				rc.setJedisPool(rc.createJedisPool(jedisProperties));
				return rc;
			}
		} else {
			return redisClient;
		}
		/*
		 * JedisPool pool = getJedisPool(jedisProperties); RedisClient rc = new
		 * RedisClient(); rc.setJedisPool(pool); return rc;
		 */
	}

	/**
	 * redis的话... StringRedisTemplate stringRedisTemplate =
	 * (StringRedisTemplate)
	 * WebApplicationContextHelper.getBean("stringRedisTemplate");
	 */
	@Override
	public SSOToken getToken(String key, long seconds) {
		key = key.toUpperCase();
		String json = redisClient.get(key);
		if (json != null) {
			SSOToken ssoToken = JSON.parseObject(json, SSOToken.class);
			if (seconds > -1) {
				redisClient.expire(key, seconds);
			}
			return ssoToken;
		} else {
			return null;
		}

	}

	@Override
	public boolean setToken(String key, SSOToken ssoToken, long seconds) {
		key = key.toUpperCase();
		String json = JSON.toJSONString(ssoToken);
		redisClient.set(key, json);

		if (seconds > -1) {
			redisClient.expire(key, seconds);
		}
		return true;
	}

	@Override
	public boolean deleteToken(String key) {
		key = key.toUpperCase();
		redisClient.del(key);
		return true;
	}

	@Override
	public <T> T getObjectInfo(String key, long expires, Class<T> c) {
		key = key.toUpperCase();
		String json = redisClient.get(key);
		if (json != null) {
			T tt = (T) JSON.parseObject(json, c);
			if (expires > -1) {
				redisClient.expire(key, expires);
			}
			return tt;
		} else {
			return null;
		}
	}

	@Override
	public <T> boolean setObjectInfo(String key, T v, long expires) {
		key = key.toUpperCase();
		String json = JSON.toJSONString(v);
		redisClient.set(key, json);

		if (expires > -1) {
			redisClient.expire(key, expires);
		}
		return true;
	}

}
