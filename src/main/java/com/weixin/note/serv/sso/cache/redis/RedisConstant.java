package com.weixin.note.serv.sso.cache.redis;

import com.weixin.note.serv.redis.key.KeyUtils;
import com.weixin.note.serv.redis.key.ValueTypeEnum;

/**
 * redis过期时间常量
 * key规则：
 * 各种业务前缀+条件({by:id=?})+过期时间({exp="+ QUARTY_HOUR +"}"或{exp=to:n/hh:mm:ss")
 * 过期时间：0代表当当天 1代表明天，2代表后天。
 * @author nianxiaoling
 *
 */
public class RedisConstant {
	/**
	 * 
	 * 用于区分key是集合类的还是单个实体类的。
	 * @author abc
	 *
	 */
	protected static final String KEY_PREFIX = "YEB:TPS:";
    //------------------------------------过期时间设置----------------------------
	public static final int FIVE_MIN	=300; 	//5分钟  
	public static final int QUARTY_HOUR	=900; 	//四分之一小时
	public static final int HALF_HOUR	=1800;
	//1小时
	public static final int ONE_HOUR	=3600;  

	//------------------------------------------【单个对象】：数据操作时自更新【状态为无效/删除时移除key】规则:exp=dd:hh:mm 指绝对时间 0:23:30 每天晚上23点执行 至   1:01:00 第二天的凌晨1点执行 |相对时间"+ ONE_HOUR +"}?号为对应的id值------------------------
	//商品表    
	public final static String KEY_INFOADV= KeyUtils.constructKey(KEY_PREFIX , ValueTypeEnum.BO ,"infoAdv",new String[]{"categoryId","placeNum"});

    
} 