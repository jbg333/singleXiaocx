package com.weixin.note.serv.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * #单机
 * 多redis连接配置
 * redis.host = 120.24.92.75
 * @author admin
 *
 */
@Component
@PropertySource(value = "classpath:/redis/redis.properties")
@ConfigurationProperties(prefix = "redis")
public class JedisProperties {
	private String host;

	@Value("${redis.connect.type}")
	private String connectType;
	
	private int port;

	private String password;
	
	
	@Value("${redis.pool.maxTotal:''}")
	private int maxTotal;

	@Value("${redis.pool.maxIdle:''}")
	private int maxIdle;

	@Value("${redis.pool.maxWaitMillis:''}")
	private int maxWaitMillis;

	@Value("${redis.pool.testOnBorrow:''}")
	private boolean testOnBorrow;
	
//	@Value("${redis.pool.blockWhenExhausted}")
	private boolean blockWhenExhausted;
	
	@Value("${redis.pool.testOnReturn}")
	private boolean testOnReturn;
	
	 
	
	@Value("${redis.master.name}")
	private String masterName;	

	
	@Value("${redis.sentinel.first}")
	private String sentinelFirst;	
	
	@Value("${redis.sentinel.second}")
	private String sentinelSecond;	
	
	@Value("${redis.sentinel.third}")
	private String sentinelThird;	
	
	
	
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isBlockWhenExhausted() {
		return blockWhenExhausted;
	}

	public void setBlockWhenExhausted(boolean blockWhenExhausted) {
		this.blockWhenExhausted = blockWhenExhausted;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
 

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public String getSentinelFirst() {
		return sentinelFirst;
	}

	public void setSentinelFirst(String sentinelFirst) {
		this.sentinelFirst = sentinelFirst;
	}

	public String getSentinelSecond() {
		return sentinelSecond;
	}

	public void setSentinelSecond(String sentinelSecond) {
		this.sentinelSecond = sentinelSecond;
	}

	public String getSentinelThird() {
		return sentinelThird;
	}

	public void setSentinelThird(String sentinelThird) {
		this.sentinelThird = sentinelThird;
	}

	public String getConnectType() {
		return connectType;
	}

	public void setConnectType(String connectType) {
		this.connectType = connectType;
	}

	 

	 
	
}
