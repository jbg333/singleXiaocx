package com.weixin.note.serv.redis.impl;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.weixin.redis.JedisProperties;

 


public class RedisUtil {
	
	public static JedisProperties getJedisProperties(){
		JedisProperties jedisProperties = new JedisProperties();
		jedisProperties.setConnectType(getValue("redis.connect.type"));
		jedisProperties.setPassword(getValue("redis.password"));
		jedisProperties.setHost( getValue("redis.host"));
		jedisProperties.setPort(Integer.parseInt(getValue("redis.port")));
		jedisProperties.setMaxIdle(Integer.parseInt(getValue("redis.pool.maxIdle")));
		jedisProperties.setMaxWaitMillis(Integer.parseInt(getValue("redis.pool.maxWaitMillis")));
		jedisProperties.setTestOnBorrow(Boolean.valueOf(getValue("redis.pool.testOnBorrow")));
		jedisProperties.setMaxTotal(Integer.parseInt(getValue("redis.pool.maxTotal")));
		jedisProperties.setTestOnBorrow(Boolean.valueOf(getValue("redis.pool.testOnBorrow")));	
		jedisProperties.setTestOnReturn(Boolean.valueOf(getValue("redis.pool.testOnReturn")));
		return jedisProperties;
		//jedisProperties.setBlockWhenExhausted(blockWhenExhausted);
	}

	
	public static JedisProperties getJedisSentinelProperties(){
		JedisProperties jedisProperties = new JedisProperties();
		jedisProperties.setConnectType(getValue("redis.connect.type"));
		jedisProperties.setMasterName(getValue("redis.master.name"));
		jedisProperties.setSentinelFirst(getValue("redis.sentinel.first"));
		jedisProperties.setSentinelSecond(getValue("redis.sentinel.second"));
		jedisProperties.setSentinelThird(getValue("redis.sentinel.third"));
		jedisProperties.setMaxIdle(Integer.parseInt(getValue("redis.pool.maxIdle")));
		jedisProperties.setMaxWaitMillis(Integer.parseInt(getValue("redis.pool.maxWaitMillis")));
		jedisProperties.setTestOnBorrow(Boolean.valueOf(getValue("redis.pool.testOnBorrow")));
		jedisProperties.setMaxTotal(Integer.parseInt(getValue("redis.pool.maxTotal")));
		jedisProperties.setTestOnBorrow(Boolean.valueOf(getValue("redis.pool.testOnBorrow")));	
		jedisProperties.setTestOnReturn(Boolean.valueOf(getValue("redis.pool.testOnReturn")));
		return jedisProperties;
		//jedisProperties.setBlockWhenExhausted(blockWhenExhausted);
	}	
	
	
	   /** 记录日志的变量 */
	
	
    /** 类实例变量 */
    private static RedisUtil      instance = null;
    /** 系统的默认自定义properties */
    public static Map<String, String> config   = null;

    /** 私有构造函数 */
    private RedisUtil() {
    }

    static {
        config = RedisUtil.getInstance().getProperties("/auth/ssoredis.properties");
    }

    /**
     * 获取实例变量
     * 
     * @return PropertiesUtil实例
     */
    public static RedisUtil getInstance() {
        if (instance == null) {
            instance = new RedisUtil();
        }
        return instance;
    }
    
    /**
     * 
     * @author zhangjinlong
     * @date 2015年10月1日
     * @param key
     * @return
     */
    public static String getValue(String key){
    	return config.get(key);
    }
    
    /**
     * 得到属性对应的值
     * @author zhangjinlong
     * @date 2015年4月24日
     * @param keyName
     * @return
     */
    public static String  getValueByKey(String keyName){
    	if (config!=null && config.containsKey(keyName)){
    		return config.get(keyName);
    	}else{
    		return null;
    	}
    }
    
  

    /**
     * 根据<code>Properties</code>文件名取得所有的键值
     * 
     * @param filename 类路径下的<code>Properties</code>文件名
     * @return
     */
    public Map<String, String> getProperties(String filename) {
        Map<String, String> map = new HashMap<String, String>();
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream(filename);
            if (is == null) {
                return map;
            }
            Properties pro = new Properties();
            pro.load(is);
            Enumeration<?> e = pro.propertyNames();
            String key = null;
            String value = null;
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                value = pro.getProperty(key);
                map.put(key, value == null ? "" : value.trim());
            }
        } catch (IOException ie) {
           ie.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ie) {
                   // logger.error(ie.getMessage());
                }
            }
        }
        return map;
    }

    /**
     * 根据<code>Properties</code>文件名取得所有的键值
     * 
     * @param filename 文件绝对路径<code>Properties</code>文件名
     * @return
     */
    public Map<String, String> getPropertiesByFilePath(String filename) {
        Map<String, String> map = new HashMap<String, String>();
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            Properties pro = new Properties();
            pro.load(is);
            Enumeration<?> e = pro.propertyNames();
            String key = null;
            String value = null;
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                value = pro.getProperty(key);
                map.put(key, value == null ? "" : value.trim());
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ie) {
                   // logger.error(ie.getMessage(),ie);
                }
            }
        }
        return map;
    }
    
    

    
}
