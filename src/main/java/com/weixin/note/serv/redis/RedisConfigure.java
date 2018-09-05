package com.weixin.note.serv.redis;

 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.weixin.note.serv.redis.impl.RedisClientImpl;
import com.weixin.note.serv.redis.impl.RedisSentinelClientImpl;


@Configuration
@EnableConfigurationProperties(JedisProperties.class)
@ConditionalOnClass(RedisClient.class)//判断这个类是否在classpath中存在  
public class RedisConfigure {

	protected final static Logger logger = LoggerFactory.getLogger(RedisConfigure.class);

	@Autowired
	private JedisProperties jedisProperties;
	


	
	@Bean("redisClient")
	public RedisClient redisClient() {
		if (jedisProperties.getConnectType().equals("single")){
			RedisClientImpl rc = new RedisClientImpl();
			rc.setJedisPool(rc.createJedisPool(jedisProperties));
			return rc;
		}else if (jedisProperties.getConnectType().equals("sentinel")){
			RedisSentinelClientImpl rc = new RedisSentinelClientImpl();
			rc.setJedisPool(rc.createJedisSentinelPool(jedisProperties));
			return rc;	
		}else{
			return null;
		}
	
	}

}
