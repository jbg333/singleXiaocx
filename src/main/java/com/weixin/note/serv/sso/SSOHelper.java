package com.weixin.note.serv.sso;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.weixin.note.serv.sso.config.SSOConfig;
import com.weixin.note.serv.sso.service.ConfigurableAbstractKissoService;
import com.weixin.note.serv.sso.token.AppTypeEnum;
import com.weixin.note.serv.sso.token.SSOToken;
import com.weixin.note.serv.sso.util.RandomUtil;
import com.weixin.note.serv.sso.util.SSOEnumUtil;
import com.weixin.note.serv.sso.util.SSOTokenContext;

/**
 * <p>
 * SSO 帮助类
 * </p>
 *
 * @author nianxl
 * @since 2016-01-21
 */
public class SSOHelper {

	protected static ConfigurableAbstractKissoService kissoService;

	/**
	 * Kisso 服务初始化
	 */
	public static ConfigurableAbstractKissoService getKissoService() {
		if (kissoService == null) {
			kissoService = new ConfigurableAbstractKissoService();
		}
		return kissoService;
	}

	/**
	 * <p>
	 * 生成 18 位随机字符串密钥<br>
	 * 替换配置文件 sso.properties 属性 sso.secretkey=随机18位字符串
	 * </p>
	 */
	public static String getSecretKey() {
		return RandomUtil.getCharacterAndNumber(18);
	}

	/**
	 * ------------------------------- 登录相关方法 -------------------------------
	 */
	/**
	 * <p>
	 * 设置加密 Cookie（登录验证成功）<br>
	 * 最后一个参数 true 销毁当前JSESSIONID. 创建可信的 JSESSIONID 防止伪造 SESSIONID 攻击
	 * </p>
	 * <p>
	 * 最后一个参数 false 只设置 cookie
	 * </p>
	 * request.setAttribute(SSOConfig.SSO_COOKIE_MAXAGE, maxAge);<br>
	 * 可以动态设置 Cookie maxAge 超时时间 ，优先于配置文件的设置，无该参数 - 默认读取配置文件数据 。<br>
	 * maxAge 定义：-1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
	 * </p>
	 *
	 * @param request
	 * @param response
	 * @param ssoToken
	 *            SSO 票据
	 * @param invalidate
	 *            销毁当前 JSESSIONID
	 * 
	 * @return 返回 token串
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, SSOToken ssoToken,
			boolean invalidate) {
		
	 	if (invalidate) {
			getKissoService().authCookie(request, response, ssoToken);
		} else {
			getKissoService().setCookie(request, response, ssoToken);
		}
	}

	
	/**
	 * APP方式保存Token
	 * 
	 * @param request
	 * @param response
	 * @param ssoToken
	 * @param invalidate
	 */
	public static String setToken(SSOToken ssoToken, HttpServletRequest request) {
		String token = ssoToken.getToken();
		if (token==null || token.trim().equals("")){
			token = ssoToken.createTokens();
		}
		
		AppTypeEnum appType = null;
		if (StringUtils.isEmpty(ssoToken.getAppType())){
			appType = SSOEnumUtil.getEnumByCode(AppTypeEnum.class, ssoToken.getAppType());
		}
		
		getKissoService().saveToCache(appType,ssoToken, request);
		return token;

	}
	

/*	
 
*/
	
	/**s
	 * 优先从缓存取
	 * <p>
	 * 获取当前请求 token<br>
	 * 该方法直接从 cookie 中解密获取 token, 常使用在登录系统及拦截器中使用 getToken(request)
	 * </p>
	 * <p>
	 * 如果该请求在登录拦截器之后请使用 attrToken(request) 防止二次解密
	 * </p>
	 *
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends SSOToken> T getSSOToken(HttpServletRequest request) {
		return (T) getKissoService().getSSOToken(request);
	}

	public static SSOToken checkIpBrowser(HttpServletRequest request, SSOToken ssoToken) {
		return getKissoService().checkIpBrowser(request, ssoToken);
	}

	
	/**
	 * <p>
	 * 从请求中获取 token 通过登录拦截器之后使用<br>
	 * 该数据为登录拦截器放入 request 中，防止二次解密
	 * </p>
	 *
	 * @param request
	 *            访问请求
	 * @return
	 */
	public static <T extends SSOToken> T attrToken(HttpServletRequest request) {
		SSOToken token =  getKissoService().attrSSOToken(request);
		if (token==null || token.equals("")){
			return (T) getKissoService().getSSOToken(request);
		}else{
			return (T) token;
		}
	}
	

	/**
	 * <p>
	 * 退出登录， 并且跳至 sso.properties 配置的属性 sso.logout.url 地址
	 * </p>
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		getKissoService().logout(request, response);
	}

	/**
	 * <p>
	 * 清理当前登录状态<br>
	 * 清理 Cookie、缓存、统计、等数据
	 * </p>
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	public static boolean clearLogin(HttpServletRequest request, HttpServletResponse response) {
		return getKissoService().clearLogin(request, response);
	}

	/**
	 * <p>
	 * 退出重定向登录页，跳至 sso.properties 配置的属性 sso.login.url 地址
	 * </p>
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		getKissoService().clearRedirectLogin(request, response);
	}

	/**
	 * <p>
	 * 获取 token 的缓存主键
	 * </p>
	 *
	 * @param request
	 *            当前请求
	 * @return
	 */
	public static String getTokenCacheKey(HttpServletRequest request,String userId,AppTypeEnum appType) {
		// return getSSOToken(request).toCacheKey(request);
		return SSOConfig.getInstance().toCacheKey(request,userId,appType);
	}

	

	/**
	 * <p>
	 * 踢出 指定用户 ID 的登录用户，退出当前系统。
	 * </p>
	 *
	 * @param userId
	 *            用户ID
	 * @return
	 */
/*	public static boolean kickLogin(Object userId, HttpServletRequest request) {
		return getKissoService().kickLogin(userId, request);
	}
	*/
	
	public static boolean kickLogin(AppTypeEnum appType, Object userId, HttpServletRequest request) {
		return getKissoService().kickLogin(appType, userId, request);
	}

	
	/**
	 * 得到当前用户
	 * @param req
	 * @param c
	 * @return
	 */
	public static <T> T getCurrentUser(HttpServletRequest req,Class<T> c){
		return getCurrentUserInfo(req,c);
	}
	
	/**
	 * 得到用户对象(优先从缓存里，如果缓存不存在，则从数据库里)
	 * @param request
	 * @param token
	 * @return
	 */
	public static <T> T getCurrentUserInfo(HttpServletRequest req,Class<T> c){
		String userId = SSOTokenContext.getInstance().getUserId();
		if (userId==null || userId.equals("")){
			userId = SSOTokenContext.getInstance().getSSOToken().getId();
		}
		if (userId==null || userId.equals("")){
			userId = SSOHelper.attrToken(req).getId();
		}
		if (userId==null || userId.equals("")){
			return null;
		}
		return getKissoService().getUserInfo(req, userId, c);
	}
	
	/**
	 * 先攀亲验是否登录，如登录再取
	 * 得到用户对象(优先从缓存里，如果缓存不存在，则从数据库里)
	 * @param request
	 * @param token
	 * @return
	 */
	public static <T> T getUserInfo(HttpServletRequest req,Object userId,Class<T> c){
		return getKissoService().getUserInfo(req, userId, c);
	}
	
	/**
	 * 得到当前用户Id
	 * @param <T>
	 * @param <T>
	 * @return
	 */
	public static String getCurrentUserId(HttpServletRequest req){
		SSOToken token = SSOHelper.attrToken(req);
		if (token!=null){
			return token.getId();
		}
		return null;
	}
	
	public static <T> T getCurrentUserName(HttpServletRequest req){
		SSOToken token = SSOHelper.attrToken(req);
		if (token!=null){
			return (T) token.getUsername();
		}
		return null;
	}
	
	public static <T> T getCurrentUserAgent(HttpServletRequest req){
		SSOToken token = SSOHelper.attrToken(req);
		if (token!=null){
			return (T) token.getUserAgent();
		}
		return null;
	}
	
}
