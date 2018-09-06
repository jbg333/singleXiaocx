
package com.weixin.note.serv.sso.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.weixin.note.serv.sso.token.AppTypeEnum;
import com.weixin.note.serv.sso.token.SSOToken;


/**
 * <p>
 * SSO 单点登录服务
 * </p>
 *
 * @author hubin
 * @since 2015-12-03
 */
public interface IKissoService {
    /**
     * <p>
     * 获取登录 SSOToken
     * </p>
     */
	SSOToken getSSOToken(HttpServletRequest request);
	
	/**
	 * 返回 token患，保存ehcache同时返回患
	 * @param token
	 * @return
	 */
	void saveToCache(AppTypeEnum appType,SSOToken token,HttpServletRequest request);
	
/*	*//**
	 * 从缓存中取得token
	 * @param request
	 *//*
	SSOToken getTokenFromCache(HttpServletRequest request,String userId);	*/

    /**
     * <p>
     * 踢出 指定用户 ID 的登录用户，退出当前系统。
     * </p>
     *
     * @param userId 用户ID
     * @return
     */
    //boolean kickLogin(Object userId,HttpServletRequest request);

    /**
     * <p>
     * 设置登录 Cookie
     * </p>
     */
    void setCookie(HttpServletRequest request, HttpServletResponse response, SSOToken SSOToken);

    /**
     * <p>
     * 清理登录状态
     * </p>
     */
    boolean clearLogin(HttpServletRequest request, HttpServletResponse response);

    /**
     * <p>
     * 退出并跳至登录页
     * </p>
     */
    void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException;

    public <T> T getUserInfo(HttpServletRequest req, Object userId, Class<T> c);

	boolean kickLogin(AppTypeEnum appType, Object userId, HttpServletRequest request);
}


