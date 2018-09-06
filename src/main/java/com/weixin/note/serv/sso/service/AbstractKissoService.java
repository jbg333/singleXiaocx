
package com.weixin.note.serv.sso.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.weixin.note.serv.sso.SSOAuthorization;
import com.weixin.note.serv.sso.SSOCache;
import com.weixin.note.serv.sso.SSOPlugin;
import com.weixin.note.serv.sso.token.AppTypeEnum;
import com.weixin.note.serv.sso.token.SSOToken;
import com.weixin.note.serv.sso.token.util.TokenFlag;
import com.weixin.note.serv.sso.util.CookieHelper;
import com.weixin.note.serv.sso.util.HttpUtil;
import com.weixin.note.serv.sso.util.RandomUtil;
import com.weixin.note.serv.sso.util.SSOEnumUtil;
import com.weixin.note.serv.util.Rt;

/**
 * <p>
 * SSO 单点登录服务抽象实现类
 * </p>
 *
 * @author hubin
 * @since 2015-12-03
 */
public abstract class AbstractKissoService extends KissoServiceSupport implements IKissoService {

	/**
	 * 获取当前请求 JWTToken
	 * <p>
	 * 从 Cookie 解密 JWTToken 使用场景，拦截器，非拦截器建议使用 attrJWTToken 减少二次解密
	 * </p>
	 *
	 * @param request
	 * @return JWTToken {@link SSOToken}
	 */
	@Override
	public SSOToken getSSOToken(HttpServletRequest request) {
		SSOToken tk = checkIpBrowser(request, cacheSSOToken(request, config.getCache()));
		/**
		 * 执行插件逻辑
		 */
		List<SSOPlugin> pluginList = config.getPluginList();
		if (pluginList != null) {
			for (SSOPlugin plugin : pluginList) {
				boolean valid = plugin.validateToken(tk);
				if (!valid) {
					return null;
				}
			}
		}
		return tk;
	}

	
	
	@Override
	public boolean kickLogin(AppTypeEnum appType, Object userId, HttpServletRequest request) {
		SSOCache cache = config.getCache();
		if (cache != null) {
			return cache.deleteToken(config.toCacheKey(request,userId.toString(), appType));
		} else {
			logger.info(" kickLogin! please implements SSOCache class.");
		}
		return false;
	}

	
	/**
	 * 保存token
	 */
	public void saveToCache(AppTypeEnum appType, SSOToken token, HttpServletRequest request) {
		if (token.getToken()==null){
			token.createTokens();
		}
		
		if (StringUtils.isNotEmpty(token.getAppType())){
			appType = SSOEnumUtil.getEnumByCode(AppTypeEnum.class, token.getAppType());
		}
		
		SSOCache cache = config.getCache();
		if (cache != null) {
			String key = config.toCacheKey(request,token.getId(),appType);
			cache.setToken(key, token, config.getCacheExpires());
		}
	}



	/**
	 * 当前访问域下设置登录Cookie
	 * <p>
	 * <p>
	 * request.setAttribute(SSOConfig.SSO_COOKIE_MAXAGE, -1); 可以设置 Cookie 超时时间
	 * ，默认读取配置文件数据 。 -1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
	 * </p>
	 *
	 * @param request
	 * @param response
	 */
	@Override
	public void setCookie(HttpServletRequest request, HttpServletResponse response, SSOToken ssoToken) {
		/**
		 * 设置加密 Cookie
		 */
		String tokens = ssoToken.getToken();
		if (tokens==null || tokens.trim().equals("")){
			tokens = ssoToken.createTokens();
		}
		Cookie ck = generateCookie(request, tokens);
		
		/**
		 * 判断 SSOToken 是否缓存处理失效
		 * <p>
		 * cache 缓存宕机，flag 设置为失效
		 * </p>
		 */
		SSOCache cache = config.getCache();
		if (cache != null) {
			
			AppTypeEnum appTypeEnum = null;
			if (StringUtils.isNotEmpty(ssoToken.getAppType())){
				appTypeEnum = SSOEnumUtil.getEnumByCode(AppTypeEnum.class, ssoToken.getAppType());
			}
			
			boolean rlt = cache.setToken(config.toCacheKey(request,ssoToken.getId(), appTypeEnum), ssoToken, config.getCacheExpires());
			if (!rlt) {
				ssoToken.setFlag(TokenFlag.CACHE_SHUT);
			}
		}

		/**
		 * 执行插件逻辑
		 */
		List<SSOPlugin> pluginList = config.getPluginList();
		if (pluginList != null) {
			for (SSOPlugin plugin : pluginList) {
				boolean login = plugin.login(request, response);
				if (!login) {
					plugin.login(request, response);
				}
			}
		}

		/**
		 * Cookie设置HttpOnly
		 */
		if (config.isCookieHttponly()) {
			CookieHelper.addHttpOnlyCookie(response, ck);
		} else {
			response.addCookie(ck);
		}
	}

	/**
	 * 当前访问域下设置登录Cookie 设置防止伪造SESSIONID攻击
	 *
	 * @param request
	 * @param response
	 */
	public void authCookie(HttpServletRequest request, HttpServletResponse response, SSOToken SSOToken) {
		CookieHelper.authJSESSIONID(request, RandomUtil.getCharacterAndNumber(8));
		this.setCookie(request, response, SSOToken);
	}

	/**
	 * 清除登录状态
	 *
	 * @param request
	 * @param response
	 * @return boolean true 成功, false 失败
	 */
	@Override
	public boolean clearLogin(HttpServletRequest request, HttpServletResponse response) {
		return logout(request, response, config.getCache());
	}

	/**
	 * <p>
	 * 重新登录 退出当前登录状态、重定向至登录页.
	 * </p>
	 *
	 * @param request
	 * @param response
	 */
	@Override
	public void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

		/* 清理当前登录状态 */
		clearLogin(request, response);

		/* redirect login page */
		String loginUrl = config.getLoginUrl();
		if ("".equals(loginUrl)) {
			String json = JSON.toJSONString(Rt.error(503,"系统已登录，请重新登录！"));			
			response.getWriter().write(json);
		} else {
			String retUrl = HttpUtil.getQueryString(request, config.getEncoding());
			logger.debug("loginAgain redirect pageUrl.." + retUrl);
			response.sendRedirect(HttpUtil.encodeRetURL(loginUrl, config.getParamReturl(), retUrl));
		}
	}

	/**
	 * SSO 退出登录
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/* delete cookie */
		logout(request, response, config.getCache());

		/* redirect logout page */
		String logoutUrl = config.getLogoutUrl();
		if ("".equals(logoutUrl)) {
			String json = JSON.toJSONString(Rt.error(504,"sso.properties Must include: sso.logout.url!"));
			response.getWriter().write(json);
		} else {
			response.sendRedirect(logoutUrl);
		}
	}

	
	/**
	 * 
	 * @param <T>
	 * @param request
	 * @param token
	 * @param c :保存key的对象类别
	 * @return 
	 */
	public <T> T getUserInfo(HttpServletRequest req, Object userId, Class<T> c){
		String key = config.toCacheKeyForUserInfo(req, userId.toString(), c);
		long expire = 60;
		/**
		 * 默认保存5秒钟,5秒之后自动过期，不需要手工清
		 */
		SSOCache cache = config.getCache();
		if (cache!=null){			
			T tt = (T) cache.getObjectInfo(key, expire, c);
			if (tt!=null){
				return tt;
			}
		}
	
		SSOAuthorization auth = config.getAuthorization();
		if (auth!=null){
			T tt = (T) auth.getObjectInfo(userId,c);
			if (cache!=null){
				cache.setObjectInfo(key, tt,expire);
			}
			return tt;
		}
		
		return null;
	}
}
