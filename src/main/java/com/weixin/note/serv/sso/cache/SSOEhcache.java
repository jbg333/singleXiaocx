package com.weixin.note.serv.sso.cache;

import com.weixin.note.serv.sso.SSOCache;
import com.weixin.note.serv.sso.token.SSOToken;

/**
 * 增加缓存  弃用(使用redis进行缓存)
 * @author admin
 *
 */
public class SSOEhcache implements SSOCache {	
	/**
	 * redis的话...
	 * StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) WebApplicationContextHelper.getBean("stringRedisTemplate");
	 */	
	@Override
	public SSOToken getToken(String key, long expires) {		
		key = key.toUpperCase();
		return null;
	}

	@Override
	public boolean setToken(String key, SSOToken ssoToken, long expires) {
		//EhcacheUtil.put(KeyConstant.CACHA_AUTH, key, ssoToken,false,expires,expires);
		key = key.toUpperCase();
		return true;
	}

	@Override
	public boolean deleteToken(String key) {
		key = key.toUpperCase();
		return true;
	}

	
	@Override
	public <T> T getObjectInfo(String key, long expires, Class<T> c) {
		key = key.toUpperCase();
		return null;
	}

	@Override
	public <T> boolean setObjectInfo(String key, T c, long expires) {
		key = key.toUpperCase();
		return true;
	}


}
