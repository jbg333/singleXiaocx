package com.weixin.note.serv.sso;

import com.weixin.note.serv.sso.token.SSOToken;

/**
 * <p>
 * SSO 缓存接口
 * </p>
 *
 * @author hubin
 * @since 2015-11-03
 */
public interface SSOCache {

    /**
     * <p>
     * 根据key获取SSO票据
     * </p>
     * <p>
     * 如果缓存服务宕机，返回 token 设置 flag 为 Token.FLAG_CACHE_SHUT
     * </p>
     *
     * @param key     关键词
     * @param expires 过期时间（延时心跳时间）
     * @return SSO票据
     */
    SSOToken getToken(String key, long expires);

    /**
     * 设置SSO票据
     *
     * @param key      关键词
     * @param ssoToken SSO票据
     * @param expires  过期时间
     */
    boolean setToken(String key, SSOToken ssoToken, long expires);
    
    
 /*   boolean setToken(String key, String ssoToken, int expires);*/

    /**
     * 删除SSO票据
     * <p>
     *
     * @param key 关键词
     */
    boolean deleteToken(String key);
    
    
    /**
     * <p>
     * 根据key获取帐户对象     * </p>
     * <p>
     * 如果缓存服务宕机，返回 token 设置 flag 为 Token.FLAG_CACHE_SHUT
     * </p>
     * @param <T>
     *
     * @param key     关键词
     * @param expires 过期时间（延时心跳时间）
     * @return SSO票据
     */
    <T> T getObjectInfo(String key, long expires,Class<T> c);

    /**
     * 设置对象
     * @param <T>
     *
     * @param key      关键词
     * @param ssoToken SSO票据
     * @param expires  过期时间
     * @return 
     */
    <T> boolean setObjectInfo(String key, T c, long expires);
    
    
}
