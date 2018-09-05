package com.weixin.note.serv.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.SortingParams;

public interface RedisClient {
	//public Jedis getJedis();

	/**
	 * 设置key的过期时间，以秒为单位
	 *
	 * @param String
	 *            key
	 * @param 时间
	 *            ,已秒为单位
	 * @return 影响的记录数
	 */
	public long expire(String key, long seconds);
	
 

    /**
     * 查询key的过期时间
     *
     * @param String
     *            key
     * @return 以秒为单位的时间表示
     * */
    public long getExpire(String key);
 
    /**
     * 取消对key过期时间的设置
     *
     * @param key
     * @return 影响的记录数
     * */
    public long persist(String key) ;
 
    
    /**
     * 删除keys对应的记录,可以是多个key
     *
     * @param String
     *            ... keys
     * @return 删除的记录数
     * */
    public long del(String... keys);
 
    /**
     * 删除key
     * @author gonghuixiaoling[14110310]
     * @date 2015年7月27日
     * @param key
     * @return
     */
    public long del(String key);
 
    /**
     * 删除一个集合的key
     * @author gonghuixiaoling[14110310]
     * @date 2015年3月22日
     * @param keys
     * @return
     */
 
    
    /**
     * 删除某个前缀的key
     * @author gonghuixiaoling[14110310]
     * @date 2015年3月23日
     * @param prefix
     * @return
     */
    public long delByPrefix(String prefix);//{
 
    
    

    /**
     * 判断key是否存在
     *
     * @param String
     *            key
     * @return boolean
     * */
    public boolean exists(String key) ;

    
    /**
     * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法
     *
     * @param String
     *            key
     * @return List<String> 集合的全部记录
     * **/
    public List<String> sort(String key);

    
    /**
     * 对List,Set,SortSet进行排序或limit
	  * public SortingParams by(java.lang.String pattern)
		* Sort by weight in keys.
		* Takes a pattern that is used in order to generate the key names of the weights used for sorting. 
		* Weight key names are obtained substituting the first occurrence of 
		* with the actual value of the elements on the list.
		* The pattern for a normal key/value pair is "keyname*" and for a value in a hash "keyname*->fieldname".
     * @param String
     *            key
     * @param SortingParams
     *            parame 定义排序类型或limit的起止位置.
     * @return List<String> 全部或部分记录
     * **/
    public List<String> sort(String key, SortingParams parame);

    /**
     * 返回指定key存储的类型
     *
     * @param String
     *            key
     * @return String string|list|set|zset|hash
     * **/
    public String type(String key);
    
    
    public <T> String set(final String key, T c);
    public <T> T get(final String key, Class<T> c);
    
	public String set(String key, String value);
	public String get(String key);


	
	public String set(String key, String value,int seconds);
	public String setex(String key, String value, int seconds);
	
	
	public String srandmember(final String key);
	
	public List<String> srandmember(String key, Integer value);
	
    /**
     * 添加一条记录，仅当给定的key不存在时才插入
     * @param String key
     * @param String value
     * @return long 状态码，1插入成功且key不存在，0未插入，key存在
     * */
    public Long setnx(String key, String value);

   
    /**
     * 从指定位置开始插入数据，插入的数据会覆盖指定位置以后的数据<br/>
     * 例:String str1="123456789";<br/>
     * 对str1操作后setRange(key,4,0000)，str1="123400009";
     * @param String  key
     * @param long offset
     * @param String  value
     * @return long value的长度
     * */
    public long setRange(String key, long offset, String value) ;

    /**
     * 在指定的key中追加value
     * @param String  key
     * @param String value
     * @return long 追加后value的长度
     * **/
    public long append(String key, String value);

   

  
    /**
     * 获取并设置指定key对应的value<br/>
     * 如果key存在返回之前的value,否则返回null
     * @param String  key
     * @param String value
     * @return String 原始value或null
     * */
    public String getSet(String key, String value);
    
    
    /**
     * 向集合中增加一条记录,如果这个值已存在，这个值对应的权重将被置为新的权重
     *
     * @param String
     *            key
     * @param double score 权重
     * @param String
     *            member 要加入的值，
     * @return 状态码 1成功，0已存在member的值
     * */
    public long zadd(String key, double score, String member) ;

    //将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
    public long zadd(String key, Map<String,Double> map) ;

    /**
     * 获取集合中元素的数量
     *
     * @param String
     *            key
     * @return 如果返回0则集合不存在
     * */
    public long zcard(String key);

    /**
     * 获取指定权重区间内集合的数量
     *
     * @param String
     *            key
     * @param double min 最小排序位置
     * @param double max 最大排序位置
     * */
    public long zcount(String key, double min, double max);

    
   

    
    /**
     * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
     *
     * @param String
     *            key
     * @param int start 开始位置(包含)
     * @param int end 结束位置(包含)
     * @return Set<String>
     * */
    public Set<String> zrange(String key, int start, int end);

 

    /**
     * 获取指定值在集合中的位置，集合排序从低到高
     *
     * @see zrevrank
     * @param String
     *            key
     * @param String
     *            member
     * @return long 位置
     * */
    public long zrank(String key, String member) ;

    /**
     * 获取指定值在集合中的位置，集合排序从高到低
     *
     * @see zrank
     * @param String key
     * @param String member
     * @return long 位置
     * */
    public long zrevrank(String key, String member);

    /**
     * 从集合中删除成员
     *
     * @param String key
     * @param String member
     * @return 返回1成功
     * */
    public long zrem(String key, String member);

   



    /**
     * 获取给定区间的元素，原始按照权重由高到低排序
     *
     * @param String  key
     * @param int start
     * @param int end
     * @return Set<String>
     * */
    public Set<String> zrevrange(String key, int start, int end) ;

    
    
    /**
     * 获取给定值在集合中的权重
     * (权重参数score，使得集合中的元素能够按score进行有序排列)
     * @param String  key
     * @param memeber
     * @return double 权重
     * */
    public double zscore(String key, String memebr);
    
    
	   /**
  * 向Set添加一条记录，如果member已存在返回0,否则返回1
  *
  * @param String
  *            key
  * @param String
  *            member
  * @return 操作码,0或1
  * */
 public long sadd(String key, String member) ;
 
 /**
  * 向Set添加多条记录
  *
  * @param String
  *            key
  * @param String
  *            member
  * @return 操作码,0或1
  * */
 public long sadd(String key, String... member);

 /**
  * 获取给定key中元素个数
  *
  * @param String
  *            key
  * @return 元素个数
  * */
 public long scard(String key) ;

 


 /**
  * 确定一个给定的值是否存在
  *
  * @param String
  *            key
  * @param String
  *            member 要判断的值
  * @return 存在返回1，不存在返回0
  * **/
 public boolean sismember(String key, String member);
 /**
  * 返回集合中的所有成员
  *
  * @param String
  *            key
  * @return 成员集合
  * */
 public Set<String> smembers(String key) ;

 
 /**
  * 不重复保存数据
  * @author gonghuixiaoling[14110310]
  * @date 2015年7月13日
  * @param key
  * @param list
  * @return
  */
	public <T> long sadd(String key, List<T> list);
	
	
	 	
 
 /**
  * 随机返回并删除名称为key的set中一个元素
  *
  * @param String
  *            key
  * @return 被删除的成员
  * */
 public String spop(String key) ;
 
 public Set<String> spop(String key,int count);
 

 /**
  * 从集合中删除指定成员
  *
  * @param String
  *            key
  * @param String
  *            member 要删除的成员
  * @return 状态码，成功返回1，成员不存在返回0
  * */
 public long srem(String key, String member);


	
	/**
	 * List长度
	 * 
	 * @param String
	 *            key
	 * @return 长度
	 * */
	public long llen(String key);
	/**
	 * 增加集合
	 * 
	 * @author gonghuixiaoling[14110310]
	 * @date 2015年3月17日
	 * @param key
	 * @param list
	 * @return
	 */
	/*public <T> long rpushByLock(String key, List<T> list);*/
	
	
	public  long lpush(String key, String... value);
	

	
	public long rpush(final String key, final String... strings);
	
	/**
	 * 重写指定位置的值
	 * 
	 * @author gonghuixiaoling[14110310]
	 * @date 2015年3月17日
	 * @param key
	 * @param index
	 * @param c
	 * @return
	 */
	public <T> String lset(String key, int index, T c);

	


	/**
	 * 添加数据，加锁
	 * 
	 * @author gonghuixiaoling[14110310]
	 * @date 2015年3月17日
	 * @param key
	 * @param list
	 * @return
	 */
	public <T> long rpush(String key, List<T> list);


	public <T> List<T> lrange(String key, Class<T> clazz);
	

	/**
	 * 获取指定范围的记录，可以做为分页使用
	 * @param <T>
	 * 
	 * @param String
	 *            key
	 * @param long start
	 * @param long end
	 * @return List
	 * */
	public <T> List<T> lrange(String key, long start, long end,Class<T> clazz);

	/**
	 * 返回所有的结果集
	 * 
	 * @param String
	 *            key
	 * @param long start
	 * @param long end
	 * @return List
	 * */
	public  List<String> lrange(String key, long start, long end);
 
	
	public  List<String> lrange(String key);
 
	
	/**
	 * 从hash中删除指定的存储
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 状态码，1成功，0失败
	 * */
	public long hdel(String key, String field);

	/**
	 * 测试hash中指定的存储是否存在
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 1存在，0不存在
	 * */
	public boolean hexists(String key, String field);

	/**
	 * 以Map的形式返回hash中的存储和值
	 * 
	 * @param String
	 *            key
	 * @return Map<Strinig,String>
	 * */
	public Map<String, String> hgetAll(String key);

	/**
	 * 获取hash中value的集合
	 *
	 * @param String
	 *            key
	 * @return List<String>
	 * */
	public List<String> hvals(String key);

	/**
	 * 获取hash中存储的个数，类似Map中size方法
	 *
	 * @param String
	 *            key
	 * @return long 存储的个数
	 * */
	public long hlen(String key);

	/**
	 * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null
	 *
	 * @param String
	 *            key
	 * @param String
	 *            ... fieids 存储位置
	 * @return List<String>
	 * */
	public List<String> hmget(String key, String... fields) ;

	/**
	 * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null
	 * 
	 * @param <T>
	 *
	 * @param String
	 *            key
	 * @param String
	 *            ... fieids 存储位置
	 * @param Class
	 *            <T> Class<T> c
	 * @return List<T>
	 * */
	public <T> List<T> hmget(String key, Class<T> c, String... fields);

	/**
	 * 得到单个对象
	 * 
	 * @author gonghuixiaoling[14110310]
	 * @date 2015年3月22日
	 * @param key
	 * @param t
	 * @param fields
	 * @return
	 */
	public <T> T hget(String key, String fields, Class<T> c);

	public String hget(String key, String fields);

	public <T> List<T> hget(String key, Class<T> c,String fields) ;

	/**
	 * 以Map的形式返回hash中的存储和值
	 * 
	 * @param String
	 *            key
	 * @return Map<Strinig,String>
	 * */
	public <T> Map<String, T> hgetAll(String key, Class<T> c);

	
	/**
	 * hash自增长度设置 Increment the number stored at field in the hash
	 * 
	 * @param key
	 * @param field
	 * @param value
	 *            (步长)
	 * @return
	 */
	public long hincrBy(String key, String field, long value);

	/**
	 * 添加对应关系，如果对应关系已存在，则覆盖
	 *
	 * @param Strin
	 *            key
	 * @param Map
	 *            <String,String> 对应关系
	 * @return 状态，成功返回OK
	 * */
	public long hset(String key, String field, Object o);
	
	

	public long hsetnx(String key, String field, Object o);
	
	
	
	/**
	 * 一次保存多个数值
	 * 
	 * @author gonghuixiaoling[14110310]
	 * @param <T>
	 * @date 2015年3月22日
	 * @param key
	 * @param map
	 * @return
	 */
	public <T> String hmset(String key, Map<String, T> map);
	
	
	public String hmsetString(String key, Map<String, String> map);
	
	
	/**
     * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用
     *
     * @param String
     *            key
     * @param long number 要减去的值
     * @return long 减指定值后的值
     * */
    public long decrBy(String key, long number);

	/**
     * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用
     *
     * @param String
     *            key
     * @param long number 要减去的值
     * @return long 减指定值后的值
     * */
    public long incrBy(String key, long number) ;

	/**
    * 减1
    * @param key
    * @return
    */
    public long decr(String key);

	/**
     * 计数加1
     * @param key
     * @return
     */
    public long incr(String key);
    
    
    
    
}
