package com.weixin.note.serv.redis.key;

public enum ValueTypeEnum {	
	STRING, 		//字符中
	LIST,			//LIST集合
	HASH,			//哈希集合
	PAGE,		//带翻页的集合
	BO,			//累计汇总类的
	SET,			//SET对象
	SORTSET,
	Count,			//计算器
	LOCK,			//锁
}
