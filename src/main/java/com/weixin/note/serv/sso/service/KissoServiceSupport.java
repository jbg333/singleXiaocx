package com.weixin.note.serv.sso.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.note.serv.sso.SSOCache;
import com.weixin.note.serv.sso.SSOPlugin;
import com.weixin.note.serv.sso.config.SSOConfig;
import com.weixin.note.serv.sso.config.SSOConstants;
import com.weixin.note.serv.sso.exception.SSOException;
import com.weixin.note.serv.sso.token.AppTypeEnum;
import com.weixin.note.serv.sso.token.SSOJwtUtils;
import com.weixin.note.serv.sso.token.SSOToken;
import com.weixin.note.serv.sso.token.util.BrowserUtils;
import com.weixin.note.serv.sso.token.util.TokenFlag;
import com.weixin.note.serv.sso.util.CookieHelper;
import com.weixin.note.serv.sso.util.DateCompare;
import com.weixin.note.serv.sso.util.HttpUtil;
import com.weixin.note.serv.sso.util.IpHelper;
import com.weixin.note.serv.sso.util.SSOEnumUtil;

/**
 * <p>
 * SSO 单点登录服务支持类
 * </p>
 *
 * @author hubin
 * @since 2015-12-03
 */
public class KissoServiceSupport {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected SSOConfig config;

	/**
	 * ------------------------------- 客户端相关方法 -------------------------------
	 */

	/**
	 * 获取当前请求 SSOToken
	 * <p>
	 * 此属性在过滤器拦截器中设置，业务系统中调用有效
	 * </p>
	 *
	 * @param request
	 * @return SSOToken {@link SSOToken}
	 */
	@SuppressWarnings("unchecked")
	public <T extends SSOToken> T attrSSOToken(HttpServletRequest request) {
		return (T) request.getAttribute(SSOConstants.SSO_TOKEN_ATTR);
	}
	
	


	/**
	 * <p>
	 * 获取当前请求 SSOToken，并校验
	 * </p>
	 *
	 * @param request
	 * @param cookieName
	 *            Cookie名称
	 * @return SSOToken
	 */
	protected SSOToken getSSOToken(HttpServletRequest request, String cookieName) {
		Map<String, String> map = HttpUtil.getHeadersInfo(request);
		String accessToken = map.get(config.getAccessTokenName());

		if (null == accessToken || "".equals(accessToken)) {
			Cookie cookies = CookieHelper.findCookieByName(request, cookieName);
			/**
			 * 解密,by nianxl
			 */
			if (null == cookies) {
				logger.debug("Unauthorized login request, ip=" + IpHelper.getIpAddr(request));
				return null;
			}
			
			SSOToken ssoToken= SSOJwtUtils.getInstance().parser(cookies.getValue(), false);
			if (ssoToken.getToken()==null) {
				ssoToken.setToken(cookies.getValue());
			}
			return ssoToken;
		}
		
		SSOToken token = SSOJwtUtils.getInstance().parser(accessToken, true);
		if (token != null && token.getToken()==null) {
			token.setToken(accessToken);
		}
		
		//if (token.gete)
		return token;
	}

	/**
	 * <p>
	 * 校验SSOToken IP 浏览器 与登录一致
	 * </p>
	 *
	 * @param request
	 * @param ssoToken
	 *            登录票据
	 * @return SSOToken {@link SSOToken}
	 */
	public SSOToken checkIpBrowser(HttpServletRequest request, SSOToken ssoToken) {
		if (null == ssoToken) {
			return null;
		}
		/**
		 * 判断请求浏览器是否合法
		 */
		if (config.isCookieBrowser() && !BrowserUtils.isLegalUserAgent(request, ssoToken.getUserAgent())) {
			logger.info("The request browser is inconsistent.");
			return null;
		}
		/**
		 * 判断请求 IP 是否合法
		 */
		if (config.isCookieCheckip()) {
			String ip = IpHelper.getIpAddr(request);
			if (ssoToken != null && ip != null && !ip.equals(ssoToken.getIp())) {
				/*
				 * logger.info(String.
				 * format("ip inconsistent! return SSOToken null, SSOToken userIp:%s, reqIp:%s"
				 * , ssoToken.getIp(), ip));
				 */
				logger.info(String.format("ip inconsistent! return SSOToken null, SSOToken userIp:%s, reqIp:%s",
						ssoToken.getIp(), ip));
				return null;
			}
		}
		return ssoToken;
	}

	/**
	 * cookie 中获取 SSOToken, 该方法未验证 IP 等其他信息。
	 * <p>
	 * <p>
	 * 1、自动设置 2、拦截器 request 中获取 3、解密 Cookie 获取
	 * </p>
	 *
	 * @param request
	 *            HTTP 请求
	 * @return
	 */
	public SSOToken getSSOTokenFromCookie(HttpServletRequest request) {
		SSOToken tk = this.attrSSOToken(request);
		if (tk == null) {
			tk = this.getSSOToken(request, config.getCookieName());
		}
		return tk;
	}

	/**
	 * ------------------------------- 登录相关方法 -------------------------------
	 */

	/**
	 * <p>
	 * 根据SSOToken生成登录信息Cookie
	 * </p>
	 *
	 * @param request
	 * @param token
	 *            SSO 登录信息票据
	 * @return Cookie 登录信息Cookie {@link Cookie}
	 */
	protected Cookie generateCookie(HttpServletRequest request, String token) {
		try {
			//
			Cookie cookie = new Cookie(config.getCookieName(), token);
			// modify by nianxl，改为对称加密，
			/*
			 * String uid =
			 * token.getId()+","+token.getTime()+","+token.getUserAgent()+","+
			 * token.getIp(); //时间搓
			 * 
			 * String cookies = Base64.encode(uid.getBytes()); Cookie cookie =
			 * new Cookie(config.getCookieName(), cookies);
			 * cookie.setPath(config.getCookiePath());
			 * cookie.setSecure(config.isCookieSecure());
			 */
			/**
			 * domain 提示
			 * <p>
			 * 有些浏览器 localhost 无法设置 cookie
			 * </p>
			 */
			String domain = config.getCookieDomain();
			if (null != domain) {
				cookie.setDomain(domain);
				if ("".equals(domain) || domain.contains("localhost")) {
					logger.warn("if you can't login, please enter normal domain. instead:" + domain);
				}
			}

			/**
			 * 设置Cookie超时时间
			 */
			int maxAge = config.getCookieMaxage();
			Integer attrMaxAge = (Integer) request.getAttribute(SSOConstants.SSO_COOKIE_MAXAGE);
			if (attrMaxAge != null) {
				maxAge = attrMaxAge;
			}
			if (maxAge >= 0) {
				cookie.setMaxAge(maxAge);
			}

			cookie.setPath(config.getCookiePath());

			return cookie;
		} catch (Exception e) {
			throw new SSOException("Generate sso cookie exception ", e);
		}
	}

	/**
	 * <p>
	 * SSOToken 是否缓存处理逻辑
	 * </p>
	 * <p>
	 * 判断 SSOToken 是否缓存 ， 如果缓存不存退出登录
	 * </p>
	 *
	 * @param request
	 * @return SSOToken {@link SSOToken}
	 */
	protected SSOToken cacheSSOToken(HttpServletRequest request, SSOCache cache) {
		/**
		 * 如果缓存不存退出登录
		 */

		if (cache != null) {
			SSOToken cookieSSOToken = getSSOTokenFromCookie(request);
			if (cookieSSOToken == null || cookieSSOToken.getId() == null) {
				logger.warn("cookieSSOToken is null.");
				return null;
			}
			
			
			AppTypeEnum appTypeEnum = null;
			if (StringUtils.isNotEmpty(cookieSSOToken.getAppType())){
				appTypeEnum = SSOEnumUtil.getEnumByCode(AppTypeEnum.class, cookieSSOToken.getAppType());
			}
			
				//return ssoToken;
			String key = config.toCacheKey(request,cookieSSOToken.getId(),appTypeEnum);
			SSOToken cacheSSOToken = cache.getToken(key, config.getCacheExpires());

			if (cacheSSOToken == null) {
				/* 开启缓存且失效，返回 null 清除 Cookie 退出 */
				logger.warn("cacheSSOToken is null.");
				return null;
			} else {
				/*
				 * 开启缓存，判断是否宕机： 1、缓存正常，返回 tk 2、缓存宕机，执行读取 Cookie 逻辑
				 */
				if (cacheSSOToken.getFlag() != TokenFlag.CACHE_SHUT) {
					/*
					 * 验证 cookie 与 cache 中 SSOToken 登录时间是否<br> 不一致返回 null
					 */
					
				    boolean result = DateCompare.sameDate(cookieSSOToken.getTime(), cacheSSOToken.getTime());
					
					
					if (result) {
						return cacheSSOToken;
					} else {
						//logger.debug("Login time is not consistent or kicked out.");
						request.setAttribute(SSOConstants.SSO_KICK_FLAG, SSOConstants.SSO_KICK_USER);
						return null;
					}
				}
			}
		}
		/**
		 * SSOToken 为 null 执行以下逻辑
		 */
		return getSSOToken(request, config.getCookieName());
	}

	/**
	 * <p>
	 * 退出当前登录状态
	 * </p>
	 *
	 * @param request
	 * @param response
	 * @return boolean true 成功, false 失败
	 */
	protected boolean logout(HttpServletRequest request, HttpServletResponse response, SSOCache cache) {
		/**
		 * SSOToken 如果开启了缓存，删除缓存记录
		 */
		if (cache != null && !SSOConstants.SSO_KICK_USER.equals(request.getAttribute(SSOConstants.SSO_KICK_FLAG))) {
			SSOToken tk = getSSOTokenFromCookie(request);
			if (tk != null) {
				
				AppTypeEnum appType = null;
				
				if (StringUtils.isNotEmpty(tk.getAppType())){
					appType = SSOEnumUtil.getEnumByCode(AppTypeEnum.class, tk.getAppType());
				}
				boolean rlt = cache.deleteToken(SSOConfig.getInstance().toCacheKey(request,tk.getId(),appType));
				if (!rlt) {
					cache.deleteToken(SSOConfig.getInstance().toCacheKey(request,tk.getId(),appType));
				}
			}
		}

		/**
		 * 执行插件逻辑
		 */
		List<SSOPlugin> pluginList = config.getPluginList();
		if (pluginList != null) {
			for (SSOPlugin plugin : pluginList) {
				boolean logout = plugin.logout(request, response);
				if (!logout) {
					plugin.logout(request, response);
				}
			}
		}

		/**
		 * 删除登录 Cookie
		 */
		return CookieHelper.clearCookieByName(request, response, config.getCookieName(), config.getCookieDomain(),
				config.getCookiePath());
	}
}
