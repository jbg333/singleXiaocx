package com.weixin.note.serv.redis.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.weixin.note.serv.exception.MyRedisException;
import com.weixin.note.serv.redis.JedisProperties;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.SortingParams;

public abstract class AbstractRedisClientImpl {	
	protected final static Logger logger = LoggerFactory.getLogger(AbstractRedisClientImpl.class);
	
	public abstract Jedis getJedis();
	public abstract void closeRedis(Jedis redis);
	
	public JedisPool createJedisPool(JedisProperties  jedisProperties) {	
		if (jedisProperties==null){
			jedisProperties =  RedisUtil.getJedisProperties();
		}
		
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(jedisProperties.getMaxTotal());
		config.setMaxIdle(jedisProperties.getMaxIdle());
		config.setMaxWaitMillis(jedisProperties.getMaxWaitMillis());
		config.setTestOnReturn(jedisProperties.isTestOnReturn());
		//config.setBlockWhenExhausted(jedisProperties.isBlockWhenExhausted());
		config.setTestOnBorrow(jedisProperties.isTestOnBorrow());
		
		JedisPool jedisPool = new JedisPool(config, jedisProperties.getHost(), jedisProperties.getPort(), Protocol.DEFAULT_TIMEOUT, jedisProperties.getPassword(), Protocol.DEFAULT_DATABASE, null);
		logger.debug("jedisPool.getResource()",jedisPool.getResource());
		return jedisPool;
	}
	
	
	public JedisSentinelPool createJedisSentinelPool(JedisProperties  jedisProperties) {	
			if (jedisProperties==null){
				jedisProperties =  RedisUtil.getJedisSentinelProperties();
			}
		
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(jedisProperties.getMaxTotal());
			config.setMaxIdle(jedisProperties.getMaxIdle());
			config.setMaxWaitMillis(jedisProperties.getMaxWaitMillis());
			config.setTestOnReturn(jedisProperties.isTestOnReturn());
			//config.setBlockWhenExhausted(jedisProperties.isBlockWhenExhausted());
			config.setTestOnBorrow(jedisProperties.isTestOnBorrow());
			
			
			String masterName = jedisProperties.getMasterName();
			String sentinel1 = jedisProperties.getSentinelFirst();
			String sentinel2= jedisProperties.getSentinelSecond();
			String sentinel3 = jedisProperties.getSentinelThird();
			Set<String> sentinels = new HashSet<String>();
			sentinels.add(sentinel1);
			sentinels.add(sentinel2);
			sentinels.add(sentinel3);
			
			JedisSentinelPool  jedisPool = new JedisSentinelPool(masterName, sentinels, config,Protocol.DEFAULT_TIMEOUT);
					 
			logger.debug("getJedisSentinelPool.getResource()",jedisPool.getResource());
			return jedisPool;
		
	}
	
	
	
	
	
	
	
	/**
	 * 得到过期时间
	 * @param expire
	 * @param timeUnit
	 * @return
	 */
	public String set(String key, String value, long second, TimeUnit timeUnit)  {
		long time = timeUnit.toSeconds(second);
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.set(key, value, "NX", "EX", time);
		}finally{
			closeRedis(jedis);
		}
	}
	
	/**
	 * 
	 * second: 单位秒
	 */
	public String set(String key, String value, int seconds)  {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String r = jedis.set(key, value);
			jedis.expire(key, seconds);
			return r;
			 //"NX", "EX", second
		}finally{
			closeRedis(jedis);
		}
	}
	
	/**
	 * 
	 * second: 单位秒
	 */
	public String setex(String key, String value, int seconds)  {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.setex(key, seconds, value);
			//return r;
			 //"NX", "EX", second
		}finally{
			closeRedis(jedis);
		}
	}
	
	
	

	

	// 清除redis的值 (换成shardedJedisClient修改 2015-07-15)
	public long del(final String... keys) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.del(keys);
		}finally{
			closeRedis(jedis);
		}		
	}

	// 清除redis的值 (换成shardedJedisClient修改 2015-07-15)
	public long del(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.del(key);
		}finally{
			closeRedis(jedis);
		}	
	}

	// 排序
	public List<String> sort(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.sort(key);
		}finally{
			closeRedis(jedis);
		}	
		
		
	}

	/**
	 * 插入数据
	 * 
	 * @param key
	 * @param offset
	 * @param value
	 * @return
	 */
	public long setRange(final String key, final long offset, final String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.setrange(key, offset, value);
		}finally{
			closeRedis(jedis);
		}		
	}

	/**
	 * 
	 * @param keysvalues
	 * @return
	 */
	public String mset(final String... keysvalues) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.mset(keysvalues);
		}finally{
			closeRedis(jedis);
		}	
	}

	/**
	 * 长度
	 * 
	 * @param key
	 * @return
	 */
	public long strlen(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.strlen(key);
		}finally{
			closeRedis(jedis);
		}
	}

	public String getRange(final String key, final long startOffset,
			final long endOffset) {
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.getrange(key, startOffset, endOffset);
		}finally{
			closeRedis(jedis);
		}
		
	}

	// 排序
	public List<String> sort(final String key, final SortingParams sortParams) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.sort(key, sortParams);
		}finally{
			closeRedis(jedis);
		}
		
		
	}

	// 排序
	public String type(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.type(key);
		}finally{
			closeRedis(jedis);
		}
		
	 
	}

	// 获取redis的值
	public String get(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.get(key);
		}finally{
			closeRedis(jedis);
		}
	
	}

	// 设置redis 控制失效时间，改为由key的参数设置
	public long expire(final String key, final long seconds) {
		if (seconds<=-1) return 0; 
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Long exp = Long.valueOf(seconds);
			long ret  = jedis.expire(key, exp.intValue());
			return  ret;
		}finally{
			closeRedis(jedis);
		}
		
		
	}


	/**
	 * 查询key的过期时间,返回给定 key 的剩余生存时间
	 * 
	 * @param key
	 * @return
	 */
	public long ttl(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return  jedis.ttl(key);
		}finally{
			closeRedis(jedis);
		}
		
 
	}

	/**
	 * 查询key的过期时间
	 * 
	 * @param key
	 * @return
	 */
	public long persist(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.persist(key);
		}finally{
			closeRedis(jedis);
		}		
		
		
	 
	}

 
 



	/**
	 * 清空所有KEY的操作
 
	 * 计数器 incr key 对key的值做加加操作,并返回新的值。
	 * 注意incr一个不是int的value会返回错误，incr一个不存在的key，则设置key为1.
	 */
	public long incr(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.incr(key);
		}finally{
			closeRedis(jedis);
		}	

	}

	/*
	 * 计数器 decr key 同对key的值做减减操作,并返回新的值。decr一个不存在key，则设置key为-1.
	 */
	public long decr(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.decr(key);
		}finally{
			closeRedis(jedis);
		}	
	}

	/*
	 * Returns all the keys matching the glob-style pattern as space separated
	 * strings. For example if you have in the database the keys "foo" and
	 * "foobar" the command "KEYS foo*" will return "foo foobar".
	 */
	public Set<String> keys(final String pattern) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.keys(pattern);
		}finally{
			closeRedis(jedis);
		}		
		
	}

	/*
	 * 常用方法 Return Boolean reply, true if the key exists, otherwise false. Test
	 * if the specified key exists. The command returns "1" if the key exists,
	 * otherwise "0" is returned. Note that even keys set with an empty string
	 * as value will return "1". Time complexity: O(1)
	 */
	public boolean exists(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.exists(key);
		}finally{
			closeRedis(jedis);
		}		
	}

	/*
	 * Get the values of all the specified keys. If one or more keys dont exist
	 * or is not of type String, a 'nil' value is returned instead of the value
	 * of the specified key, but the operation never fails.
	 */
	public List<String> hmget(final String key, final String... fields) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hmget(key, fields);
		}finally{
			closeRedis(jedis);
		}	
	}

	/*
	 * GETSET is an atomic set this value and return the old value command. Set
	 * key to the string value and return the old value stored at key. The
	 * string can't be longer than 1073741824 bytes (1 GB).
	 */
	public String getSet(final String key, final String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.getSet(key, value);
		}finally{
			closeRedis(jedis);
		}			
	}

	// =====================Hash===============================
	/*
	 * Set the specified hash field to the specified value. If key does not
	 * exist, a new key holding a hash is created. Returns:If the field already
	 * exists, and the HSET just produced an update of the value, 0 is returned,
	 * otherwise if a new field is created 1 is returned.
	 */
	public long hset(final String key, final String field, final String value) {
 
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hset(key, field, value);	
		}finally{
			closeRedis(jedis);
		}	
	}
	
  /**
   * Set the specified hash field to the specified value if the field not exists. <b>Time
   * complexity:</b> O(1)
   * @param key
   * @param field
   * @param value
   * @return If the field already exists, 0 is returned, otherwise if a new field is created 1 is
   *         returned.
   */
	public long hsetnx(final String key, final String field, final String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hsetnx(key, field, value);
		}finally{
			closeRedis(jedis);
		}	
	}

	/*
	 * If key holds a hash, retrieve the value associated to the specified
	 * field. If the field is not found or the key does not exist, a special
	 * 'nil' value is returned.
	 */
	public String hget(final String key, final String field) {		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hget(key, field);
		}finally{
			closeRedis(jedis);
		}			
	}

	public long decrBy(final String key, final long number) {
 
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.decrBy(key, number);
		}finally{
			closeRedis(jedis);
		}				
	}

	public long incrBy(final String key, final long number) {
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.incrBy(key, number);
		}finally{
			closeRedis(jedis);
		}						
	}

	/**
	 * 字符串操作，在指定的String中再添加新的字符串
	 * 
	 * @author gonghuixiaoling[14110310]
	 * @date 2015年3月17日
	 * @param key
	 * @param value
	 * @return
	 */
	public long append(final String key, final String value) {
 
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.append(key, value);
		}finally{
			closeRedis(jedis);
		}			
	}

 

	/*
	 * Increment the number stored at field in the hash at key by value. If key
	 * does not exist, a new key holding a hash is created. If field does not
	 * exist or holds a string, the value is set to 0 before applying the
	 * operation. Since the value argument is signed you can use this command to
	 * perform both increments and decrements. The range of values supported by
	 * HINCRBY is limited to 64 bit signed integers. Returns: Integer reply The
	 * new value at field after the increment operation.
	 */
	public long hincrBy(final String key, final String field, final long value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hincrBy(key, field, value);
		}finally{
			closeRedis(jedis);
		}			
	}

	
	/*
	 * Test for existence of a specified field in a hash. Returns: Return 1 if
	 * the hash stored at key contains the specified field. Return 0 if the key
	 * is not found or the field is not present.
	 */
	public boolean hexists(final String key, final String field) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hexists(key, field);
		}finally{
			closeRedis(jedis);
		}			
	}

	/*
	 * Remove the specified field from an hash stored at key. Returns: If the
	 * field was present in the hash it is deleted and 1 is returned, otherwise
	 * 0 is returned and no operation is performed.
	 */
	public long hdel(final String key, final String... fields) {
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hdel(key, fields);
		}finally{
			closeRedis(jedis);
		}				
	}

	/*
	 * Return the number of items in a hash. Returns: The number of entries
	 * (fields) contained in the hash stored at key. If the specified key does
	 * not exist, 0 is returned assuming an empty hash.
	 */
	public long hlen(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hlen(key);
		}finally{
			closeRedis(jedis);
		}
		
	}

	/*
	 * Return all the fields in a hash.
	 * 
	 * Returns:All the fields names contained into a hash.
	 */
	public Set<String> hkeys(final String key) {

		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hkeys(key);
		}finally{
			closeRedis(jedis);
		}		
	}

	/*
	 * Return all the values in a hash.
	 * 
	 * Returns:All the fields values contained into a hash.
	 */
	public List<String> hvals(final String key) {
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hvals(key);
		}finally{
			closeRedis(jedis);
		}		
	}

	/*
	 * Return all the fields and associated values in a hash.
	 * 
	 * Returns:All the fields and values contained into a hash.
	 */
	public Map<String, String> hgetAll(final String key) {


		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hgetAll(key);
		}finally{
			closeRedis(jedis);
		}
	}

	// =====================List===============================
	/*
	 * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
	 * stored at key. If the key does not exist an empty list is created just
	 * before the append operation. If the key exists but is not a List an error
	 * is returned.
	 */
	public long lpush(final String key, final String... strings) {

		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.lpush(key, strings);
		}finally{
			closeRedis(jedis);
		}		
	}

	/*
	 * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
	 * stored at key. If the key does not exist an empty list is created just
	 * before the append operation. If the key exists but is not a List an error
	 * is returned.
	 */
	public long rpush(final String key, final String... strings) {
 
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.rpush(key, strings);
		}finally{
			closeRedis(jedis);
		}				
	}

	/*
	 * Return the length of the list stored at the specified key. If the key
	 * does not exist zero is returned (the same behaviour as for empty lists).
	 * If the value stored at key is not a list an error is returned.
	 */
	public long llen(final String key) {

		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.llen(key);
		}finally{
			closeRedis(jedis);
		}			
	}

	/*
	 * Return the specified elements of the list stored at the specified key.
	 * Start and end are zero-based indexes. 0 is the first element of the list
	 * (the list head), 1 the next element and so on. For example LRANGE foobar
	 * 0 2 will return the first three elements of the list. start and end can
	 * also be negative numbers indicating offsets from the end of the list. For
	 * example -1 is the last element of the list, -2 the penultimate element
	 * and so on. Consistency with range functions in various programming
	 * languages Note that if you have a list of numbers from 0 to 100, LRANGE 0
	 * 10 will return 11 elements, that is, rightmost item is included. This may
	 * or may not be consistent with behavior of range-related functions in your
	 * programming language of choice (think Ruby's Range.new, Array#slice or
	 * Python's range() function). LRANGE behavior is consistent with one of
	 * Tcl. Out-of-range indexes Indexes out of range will not produce an error:
	 * if start is over the end of the list, or start > end, an empty list is
	 * returned. If end is over the end of the list Redis will threat it just
	 * like the last element of the list.
	 */
	public List<String> lrange(final String key, final long start,
			final long end) {

		Jedis jedis = null;
		try {
			jedis = getJedis();
			
			
			
			return jedis.lrange(key, start, end);
		}finally{
			closeRedis(jedis);
		}	
	}

	/*
	 * Trim an existing list so that it will contain only the specified range of
	 * elements specified. Start and end are zero-based indexes. 0 is the first
	 * element of the list (the list head), 1 the next element and so on. For
	 * example LTRIM foobar 0 2 will modify the list stored at foobar key so
	 * that only the first three elements of the list will remain. start and end
	 * can also be negative numbers indicating offsets from the end of the list.
	 * For example -1 is the last element of the list, -2 the penultimate
	 * element and so on. Indexes out of range will not produce an error: if
	 * start is over the end of the list, or start > end, an empty list is left
	 * as value. If end over the end of the list Redis will threat it just like
	 * the last element of the list. Hint: the obvious use of LTRIM is together
	 * with LPUSH/RPUSH. For example: lpush("mylist", "someelement");
	 * ltrim("mylist", 0, 99); * The above two commands will push elements in
	 * the list taking care that the list will not grow without limits. This is
	 * very useful when using Redis to store logs for example. It is important
	 * to note that when used in this way LTRIM is an O(1) operation because in
	 * the average case just one element is removed from the tail of the list.
	 */
	public String ltrim(final String key, final long start, final long end) {
 
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.ltrim(key, start, end);
		}finally{
			closeRedis(jedis);
		}			
	}

	
	
	/**
	 * 覆盖List中指定位置的值 Set a new value as the element at index position of the
	 * List at key. Out of range indexes will generate an error.
	 */
	public String lset(final String key, final long index, final String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.lset(key, index, value);
		}finally{
			closeRedis(jedis);
		}			
	}

	/*
	 * 
	 * Remove the first count occurrences of the value element from the list. If
	 * count is zero all the elements are removed. If count is negative elements
	 * are removed from tail to head, instead to go from head to tail that is
	 * the normal behaviour. So for example LREM with count -2 and hello as
	 * value to remove against the list (a,b,c,hello,x,hello,hello) will lave
	 * the list (a,b,c,hello,x). The number of removed elements is returned as
	 * an integer, see below for more information about the returned value. Note
	 * that non existing keys are considered like empty lists by LREM, so LREM
	 * against non existing keys will always return 0.
	 */
	public long lrem(final String key, final long count, final String value) {

		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.lrem(key, count, value);
		}finally{
			closeRedis(jedis);
		}		
	}

	/*
	 * Atomically return and remove the first (LPOP) or last (RPOP) element of
	 * the list. For example if the list contains the elements "a","b","c" LPOP
	 * will return "a" and the list will become "b","c". If the key does not
	 * exist or the list is already empty the special value 'nil' is returned.
	 */
	public String lpop(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.lpop(key);
		}finally{
			closeRedis(jedis);
		}
		
		
	}

	/*
	 * Atomically return and remove the first (LPOP) or last (RPOP) element of
	 * the list. For example if the list contains the elements "a","b","c" LPOP
	 * will return "a" and the list will become "b","c". If the key does not
	 * exist or the list is already empty the special value 'nil' is returned.
	 */
	public String rpop(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.rpop(key);
		}finally{
			closeRedis(jedis);
		}		
	}

 
	 

	/*
	 * Atomically return and remove the last (tail) element of the srckey list,
	 * and push the element as the first (head) element of the dstkey list. For
	 * example if the source list contains the elements "a","b","c" and the
	 * destination list contains the elements "foo","bar" after an RPOPLPUSH
	 * command the content of the two lists will be "a","b" and "c","foo","bar".
	 * If the key does not exist or the list is already empty the special value
	 * 'nil' is returned. If the srckey and dstkey are the same the operation is
	 * equivalent to removing the last element from the list and pusing it as
	 * first element of the list, so it's a "list rotation" command.
	 */
	// /换成shardedJedisClient修改 2015-07-15
	// public String rpoplpush(final String srckey, final String dstkey) {
	// return redisClient.execute(new ShardedJedisAction<String>() {
	// 
	// public String doAction(Jedis jedis) {
	// return jedis.rpoplpush(srckey, dstkey);
	// }
	// });
	// }

	// =====================Set===============================
	/**
	 * 向名称为key的set中添加元素member Add the specified member to the set value stored
	 * at key. If member is already a member of the set no operation is
	 * performed. If key does not exist a new set with the specified member as
	 * sole member is created. If the key exists but does not hold a set value
	 * an error is returned. Returns:Integer reply, specifically: 1 if the new
	 * element was added 0 if the element was already a member of the set
	 */
	public long sadd(final String key, final String... members) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.sadd(key, members);
		}finally{
			closeRedis(jedis);
		}		
	 
	}

	/**
	 * add by 原涌超
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public long sadd(final String key, final Set<String> members) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.sadd(key, members.toArray(new String[] {}));
		}finally{
			closeRedis(jedis);
		}			
		
	}

	/**
	 * 删除名称为key的set中的元素member Remove the specified member from the set value
	 * stored at key. If member was not a member of the set no operation is
	 * performed. If key does not hold a set value an error is returned.
	 */
	public long srem(final String key, final String... members) {
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.srem(key, members);
		}finally{
			closeRedis(jedis);
		}			
	}

	/**
	 * 随机返回并删除名称为key的set中一个元素 Remove a random element from a Set returning it as
	 * return value. If the Set is empty or the key does not exist, a nil object
	 * is returned. The srandmember(String) command does a similar work but the
	 * returned element is not removed from the Set.
	 * 
	 */
	public String spop(final String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.spop(key);
		}finally{
			closeRedis(jedis);
		}			
		
	 
	}
	
	/**
	 * 返回多个随机数
	 */
	public Set<String> spop(final String key,final int count) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.spop(key,count);
		}finally{
			closeRedis(jedis);
		}			
		
	 
	}

	/**
	 * 随机返回名称为key的set的一个元素 Return a random element from a Set, without removing
	 * the element. If the Set is empty or the key does not exist, a nil object
	 * is returned. The SPOP command does a similar work but the returned
	 * element is popped (removed) from the Set.
	 */
	public String srandmember(final String key) {
 
		
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.srandmember(key);
		}finally{
			closeRedis(jedis);
		}			
	}

	/**
	 * 将member元素从名称为srckey的集合移到名称为dstkey的集合 Move the specifided member from the
	 * set at srckey to the set at dstkey. This operation is atomic, in every
	 * given moment the element will appear to be in the source or destination
	 * set for accessing clients. If the source set does not exist or does not
	 * contain the specified element no operation is performed and zero is
	 * returned, otherwise the element is removed from the source set and added
	 * to the destination set. On success one is returned, even if the element
	 * was already present in the destination set. An error is raised if the
	 * source or destination keys contain a non Set value.
	 * 
	 */
	// public long smove(final String srckey, final String dstkey,
	// final String member) {
	// return redisClient.execute(new ShardedJedisAction<long>() {
	// 
	// public long doAction(Jedis jedis) {
	// return jedis.smove(srckey, dstkey, member);
	// }
	// });
	// }

	/**
	 * 返回名称为key的set的基数 Return the set cardinality (number of elements). If the
	 * key does not exist 0 is returned, like for empty sets.
	 */
	public long scard(final String key) {
 
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.scard(key);
		}finally{
			closeRedis(jedis);
		}			
	}

	/**
	 * sismember(key, member) ：测试member是否是名称为key的set的元素 Return 1 if member is a
	 * member of the set stored at key, otherwise 0 is returned.
	 */
	public boolean sismember(final String key, final String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.sismember(key, member);
		}finally{
			closeRedis(jedis);
		}			
		
 
	}


	/**
	 * 返回名称为key的set的所有元素 Return all the members (elements) of the set value
	 * stored at key. This is just syntax glue for SINTER.
	 */
	public Set<String> smembers(final String key) {
 
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.smembers(key);
		}finally{
			closeRedis(jedis);
		}			
	}

	// =====================Sorted Set===============================
	/*
	 * Add the specified member having the specifeid score to the sorted set
	 * stored at key. If member is already a member of the sorted set the score
	 * is updated, and the element reinserted in the right position to ensure
	 * sorting. If key does not exist a new sorted set with the specified member
	 * as sole member is crated. If the key exists but does not hold a sorted
	 * set value an error is returned. The score value can be the string
	 * representation of a double precision floating point number.
	 */
	public long zadd(final String key, final double score, final String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			
			return jedis.zadd(key, score, member);
		}finally{
			closeRedis(jedis);
		}	
		
 
	}

	public long zadd(String key, Map<String,Double> map){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			
			return jedis.zadd(key, map);
		}finally{
			closeRedis(jedis);
		}	
	}
	
	
	/*
	 * Remove the specified member from the sorted set value stored at key. If
	 * member was not a member of the set no operation is performed. If key does
	 * not not hold a set value an error is returned.
	 */
	public long zrem(final String key, final String... members) {
 
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrem(key, members);
		}finally{
			closeRedis(jedis);
		}			
	}

	/*
	 * If member already exists in the sorted set adds the increment to its
	 * score and updates the position of the element in the sorted set
	 * accordingly. If member does not already exist in the sorted set it is
	 * added with increment as score (that is, like if the previous score was
	 * virtually zero). If key does not exist a new sorted set with the
	 * specified member as sole member is crated. If the key exists but does not
	 * hold a sorted set value an error is returned. The score value can be the
	 * string representation of a double precision floating point number. It's
	 * possible to provide a negative value to perform a decrement. For an
	 * introduction to sorted sets check the Introduction to Redis data types
	 * page.
	 */
	public Double zincrby(final String key, final double score,
			final String member) {
 
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zincrby(key, score, member);
		}finally{
			closeRedis(jedis);
		}			
	}

	/*
	 * Return the rank (or index) or member in the sorted set at key, with
	 * scores being ordered from low to high. When the given member does not
	 * exist in the sorted set, the special value 'nil' is returned. The
	 * returned rank (or index) of the member is 0-based for both commands.
	 */
	public long zrank(final String key, final String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrank(key, member);
		}finally{
			closeRedis(jedis);
		}			
		
	 
	}

	/*
	 * Return the rank (or index) or member in the sorted set at key, with
	 * scores being ordered from high to low. When the given member does not
	 * exist in the sorted set, the special value 'nil' is returned. The
	 * returned rank (or index) of the member is 0-based for both commands.
	 * Returns:Integer reply or a nil bulk reply, specifically: the rank of the
	 * element as an integer reply if the element exists. A nil bulk reply if
	 * there is no such element.
	 */
	public long zrevrank(final String key, final String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrevrank(key, member);
		}finally{
			closeRedis(jedis);
		}			
		
	
	}

	// 类似lrange操作，从集合中取指定区间的元素。返回的是有序结果
	public Set<String> zrange(final String key, int start, int end) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrange(key, start, end);
		}finally{
			closeRedis(jedis);
		}			
		
	 
	}
	

	

	// 从集合中取指定区间的元素。返回结果是按score逆序的
	public Set<String> zrevrange(final String key, int start,
			int end) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrevrange(key, start, end);
		}finally{
			closeRedis(jedis);
		}			
		
		
	}

	// 返回集合中score在给定区间的数量
	public long zcount(final String key, final double min, final double max) {
 
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zcount(key, min, max);
		}finally{
			closeRedis(jedis);
		}			
				
	}

	/*
	 * 返回集合中元素个数 Return the sorted set cardinality (number of elements). If the
	 * key does not exist 0 is returned, like for empty sorted sets.
	 */
	public long zcard(final String key) {
 
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zcard(key);
		}finally{
			closeRedis(jedis);
		}			
	}

	/*
	 * Return the score of the specified element of the sorted set at key. If
	 * the specified element does not exist in the sorted set, or the key does
	 * not exist at all, a special 'nil' value is returned.
	 */
	public double zscore(final String key, final String member) {
 
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zscore(key, member);
		}finally{
			closeRedis(jedis);
		}		
	}

	/*
	 * 
	 * Return the score of the specified element of the sorted set at key. If
	 * the specified element does not exist in the sorted set, or the key does
	 * not exist at all, a special 'nil' value is returned.
	 */
	public long linsert(final String key, final LIST_POSITION where,
			final String pivot, final String value) {
 
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.linsert(key, where, pivot, value);
		}finally{
			closeRedis(jedis);
		}				
	}

	/**
	 * 获取List中指定位置的值
	 * 
	 * @param String
	 *            key
	 * @param int index 位置
	 * @return 值
	 * **/
	public String lindex(final String key, final int index) {
 
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.lindex(key, index);
		}finally{
			closeRedis(jedis);
		}			
	}
	

	
	public Long setnx(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.setnx(key, value);
		}finally{
			closeRedis(jedis);
		}
	}





	
	public long getExpire(String key) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.ttl(key);
		}finally{
			closeRedis(jedis);
		}
	}


 
	

	
	public long hdel(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hdel(key, field);
		}finally{
			closeRedis(jedis);
		}	
	}


	
	public long zrem(String key, String member) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrem(key, member);
		}catch (Exception e) {
			throw new MyRedisException("调用jedis失败",e);
		}finally{
			closeRedis(jedis);
		}		
	}
	
	


	
	public long srem(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.srem(key, member);
		}catch (Exception e) {
			throw new MyRedisException("调用jedis失败",e);
		}finally{
			closeRedis(jedis);
		}	
	}



	
	public long sadd(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.sadd(key, member);
		}catch (Exception e) {
			throw new MyRedisException("调用jedis失败",e);
		}finally{
			closeRedis(jedis);
		}	
	}

	
	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.set(key, value);
		}catch (Exception e) {
			throw new MyRedisException("调用jedis失败",e);
		}finally{
			closeRedis(jedis);
		}
	}

	/*
	 * 获取单个对象 常用方法 add by 原涌超
	 */
	public <T> T get(final String key, Class<T> c) {
		String result = this.get(key);
		if (StringUtils.isNotEmpty(result)) {
			//T tt = (T) JacksonMapper.jsonToBean(result, c);
			T tt = (T)JSON.parseObject(result, c);
			return tt;
		} else {
			return null;
		}
	}
	

	/*
	 * 返回状态码 存储单个对象 常用方法
	 */
	public <T> String set(final String key, T c) {
		
		//final String value = JacksonMapper.beanToJson(c);
		final String value =JSON.toJSONString(c);
		return set(key, value);
    }


	/*
	 * 存储单个对象同时设置失效时间 The command is exactly equivalent to the following group
	 * of commands: SET + EXPIRE. The operation is atomic.
	 */
	public <T> String setex(final String key, T c, final long seconds) {
		Jedis jedis = null;
		final String value = JSON.toJSONString(c);
		Long exp =  Long.valueOf(seconds);
		try {
			jedis = getJedis();
			return jedis.setex(key, exp.intValue(), value); //.setex(key, value, "NX", "PX", time);
		}finally{
			closeRedis(jedis);
		}
	}
	
    public long del(Set<String> keys) {
    	if (keys==null){
    		return 0;
    	}
    	String[] aryKeys = (String[]) keys.toArray(new String[0]);    
        return del(aryKeys);
    }
	
    public long delByPrefix(String prefix){
    	if (prefix==null){
    		return 0;
    	}
    	/*Set<String> keys = keys(prefix+"*");*/
    	return del(prefix+"*");
    }
	
    
    public long del(List<String> keys) {
    	if (keys==null){
    		return 0;
    	}    	
    	String[] ary = (String[]) keys.toArray(new String[keys.size()]);
        return del(ary);
    }
    
    
    public <T> long sadd(String key, List<T> list) {
		if (list==null || list.size()<=0) return 0;
		String[] jsons = new String[list.size()];
		int i=0;
		for (T c : list) {
			final String json = JSON.toJSONString(c);
			jsons[i++] = json;
		}
		long count = sadd(key, jsons);
		return count;
	}
    
    
    /**
     * 
     * @param key
     * @param list
     * @return
     */
  //放到子类实现
	/*public <T> long rpushByLock(String key, List<T> list) {
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

	}*/
	
	
	public <T> String lset(String key, int index, T c) {
		final String json = JSON.toJSONString(c);
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.lset(key, index, json);
		}finally{
			closeRedis(jedis);
		}	
		
	}
	
	public <T> T lindex(String key, int index, Class<T> c) {
		String result = lindex(key, index);
		if (StringUtils.isNotBlank(result)) {
			T tt = (T) JSON.parseObject(result, c);
			return tt;
		} else {
			return null;
		}
	}

	
	/**
	 * 获取指定范围的记录，可以做为分页使用
	 * 
	 * @param <T>
	 * @param String
	 *            key
	 * @param long start
	 * @param long end
	 * @return List
	 * */
	public <T> List<T> lrange(String key, Class<T> c, long start, long end) {
		List<String> list = lrange(key, start, end);
		if (list == null || list.size() == 0) {
			return null;
		}
		List<T> ret = new ArrayList<T>();
		for (String result : list) {
			T tt = (T) JSON.parseObject(result, c);
			ret.add(tt);
		}
		return ret;

	}
	
	/**
	 * 得到list所有的类型
	 * @param key
	 * @param c
	 * @return
	 */
	public <T> List<T> lrange(String key, Class<T> c){
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			long start=0l;
			long end =  jedis.llen(key);// LLEN mylis			
			List<String> list= jedis.lrange(key, start, end);
			List<T> ret = new ArrayList<T>();
			for (String result : list) {
				T tt = (T) JSON.parseObject(result, c);
				ret.add(tt);
			}
			return ret;
			
		}finally{
			closeRedis(jedis);
		}
	}
	
	
	
	/**
	 * 添加数据，加锁 ，追加数据到list头部
	 * 
	 * @author gonghuixiaoling[14110310]
	 * @date 2015年3月17日
	 * @param key
	 * @param list
	 * @return
	 */
	//放到子类实现
	/*public <T> long lpush(String key, List<T> list) {
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
	*/
	
	public <T> List<T> lrange(String key,long start,long end,Class<T> clazz) {		
		if (end <= 0) {
			return null;
		}
		List<String> list = lrange(key, start, end);
		
		List<T> re = new ArrayList<T>();
		
		for (String json : list) {
			T tt = JSON.parseObject(json, clazz);
			re.add(tt);
		}		
		return re;
	}


	public <T> List<T> hmget(String key, Class<T> c, String... fields) {
		List<String> list = hmget(key, fields);
		List<T> result = new ArrayList<T>();
		if (list != null && list.size() > 0) {
			for (String s : list) {
				result.add((T) JSON.parseObject(s, c));
			}
		}
		return result;
	}

	public <T> List<T> hget(String key, Class<T> c,String fields) {
		String json = hget(key, fields);
		if (json == null) {
			return null;
		}
		List<T> tt = (List<T>) JSON.parseArray(json, c);
		return tt;
	}
	
 
	public <T> T hget(String key, String fields, Class<T> c) {
		String json = this.hget(key, fields);
		T tt = null;
		if(json!=null){
			tt = (T) JSON.parseObject(json, c);
		}
		return tt;
	}
	
	public <T> Map<String, T> hgetAll(String key, Class<T> c) {
		Map<String, T> ret = new HashMap<String, T>();

		Map<String, String> map = hgetAll(key);
		for (String field : map.keySet()) {
			String json = map.get(field);
			T tt = (T) JSON.parseObject(json, c);
			ret.put(field, tt);
		}
		return ret;
	}
	
	
	public long hset(String key, String field, Object o) {
		String json = "";
		json = JSON.toJSONString(o);
		return hset(key, field, json);
	}
	
	
	public long hsetnx(String key, String field, Object o) {
		String json = "";
		json = JSON.toJSONString(o);
		return hsetnx(key, field, json);
	}
	
	
	public String hmsetString(String key, Map<String, String> map) {
		if (map.size() > 0) {
			Jedis jedis = null;
			try {
				jedis = getJedis();
				return jedis.hmset(key, map);	
			}finally{
				closeRedis(jedis);
			}
		} else {
			return null;
		}
	}

	
	public <T> String hmset(String key, Map<String, T> map) {
		Map<String, String> ret = new HashMap<String, String>();
		for (String field : map.keySet()) {
			Object c = map.get(field);
			if (c instanceof String) {
				ret.put(field, (String) c);
			} else {
				String json = JSON.toJSONString(c);
				ret.put(field, json);
			}
		}
		if (ret.size() > 0) {
			Jedis jedis = null;
			try {
				jedis = getJedis();
				return jedis.hmset(key, ret);	
			}finally{
				closeRedis(jedis);
			}	
		
		
		} else {
			return null;
		}
	}

	/*
	
	public <T> List<T> lrange(String key, Class<T> clazz) {
		long start = 0l;
		long end = llen(key);
		return this.lrange(key, start, end, clazz);
		return null;
	}*/

	
	public   List<String> lrange(String key) {
		long start = 0l;
		long end = llen(key);
		return lrange(key, start, end);
	}


	//@Override
	public List<String> srandmember(String key, Integer value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			 //"NX", "EX", second
			return jedis.srandmember(key, value);
		}finally{
			closeRedis(jedis);
		}
	}

}
