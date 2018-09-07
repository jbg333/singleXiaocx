package com.weixin.note.serv.sso.config;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.weixin.note.serv.sso.SSOAuthorization;
import com.weixin.note.serv.sso.SSOCache;
import com.weixin.note.serv.sso.SSOPlugin;
import com.weixin.note.serv.sso.cache.SSOEhcache;
import com.weixin.note.serv.sso.cache.redis.SSORedis;
import com.weixin.note.serv.sso.jwt.RSAUtils;
import com.weixin.note.serv.sso.token.AppTypeEnum;
import com.weixin.note.serv.sso.token.SSOToken;

/**
 * <p>
 * SSO 配置文件解析
 * </p>
 *
 * @author hubin
 * @since 2015-12-05
 */
public class SSOConfig {
	protected final static Logger logger = LoggerFactory.getLogger(SSOConfig.class);

	// 当前应用环间
	// private static ApplicationContext applicationContext;
	private static RestTemplate restTemplate;

	private String encoding = SSOConstants.ENCODING;

	private String authServiceUrl; // 判断授权是否存在，在有单点登录系统时有效。如果单个系统，请别配置，否则会出现死循环
	private String userInfoServiceUrl; // 得到用户信息的服务提供url

	private boolean ajaxEnable = false;

	private String accessTokenName = "token";
	private String accessUserId = "userId";
	/* @Value("${sso.config.cookieName}") */
	private String cookieName = "uid";
	/* @Value("${sso.config.cookieDomain}") */
	private String cookieDomain = "";
	/* @Value("${sso.config.cookiePath}") */
	private String cookiePath = "/";
	/* @Value("${sso.config.cookieSecure}") */
	private boolean cookieSecure = false;
	/* @Value("${sso.config.cookieHttponly}") */
	private boolean cookieHttponly = true;
	/* @Value("${sso.config.cookieMaxage}") */
	private int cookieMaxage = -1;
	/* @Value("${sso.config.cookieBrowser}") */
	private boolean cookieBrowser = false;
	/* @Value("${sso.config.cookieCheckip}") */
	private boolean cookieCheckip = false;
	/* @Value("${sso.config.loginUrl}") */
	private String loginUrl = "";
	/* @Value("${sso.config.logoutUrl}") */
	private String logoutUrl = "";
	private String paramReturl = "ReturnURL";
	/* @Value("${sso.config.cacheExpires}") */
	// private int cacheExpires = -1;
	private long cacheExpires = 60 * 60 * 24 * 14; // 2个周
	private SSOToken ssoToken;
	/* @Value("${sso.config.cacheEnable}") */
	private boolean cacheEnable = true;

	// add by nianxl key
	/* @Value("${sso.config.publicKey}") */
	private String publicKey = RSAUtils.public_exponent;
	/* @Value("${sso.config.privateKey}") */
	private String privateKey = RSAUtils.private_exponent;
	/* @Value("${sso.config.overUrl}") */
	private String overUrl;
	/* @Value("${sso.config.successUrl}") */
	private String successUrl;

	/**
	 * 权限认证（默认 false）
	 */
	private boolean permissionUri = false;

	/**
	 * 插件列表
	 */
	private List<SSOPlugin> pluginList;
	private SSOCache cache;

	private static SSOConfig SSO_CONFIG = null;

	private static SSOAuthorization authorization;

	private boolean redisEnable = false;
	private String reidsKeyPrefix;

	public SSOConfig() {
		/* 支持 setInstance 设置初始化 */
	}

	/**
	 * new 当前对象
	 */
	public static SSOConfig getInstance() {
		if (SSO_CONFIG == null) {
			try {
				Thread.sleep(300);
				//synchronized (EhcacheUtil.class) {
					if (SSO_CONFIG == null) {// 二次检查
						SSO_CONFIG = new SSOConfig();
						;
					}
				//}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return SSO_CONFIG;
	}

	public static SSOConfig init(SSOConfig ssoConfig) {
		/**
		 * 默认缓存
		 */
		if (ssoConfig != null && ssoConfig.isCacheEnable() && ssoConfig.getCache() == null) {
			if (ssoConfig.isRedisEnable()) {
				ssoConfig.setCache(new SSORedis());
			} else {
				ssoConfig.setCache(new SSOEhcache());
			}
		}

		SSO_CONFIG = ssoConfig;
		return ssoConfig;
	}

	public static String getSSOEncoding() {
		return getInstance().getEncoding();
	}

	public String getEncoding() {
		return encoding;
	}

	public SSOConfig setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public String getCookieName() {
		return cookieName;
	}

	public String getAccessTokenName() {
		return accessTokenName;
	}

	public void setAccessTokenName(String accessTokenName) {
		this.accessTokenName = accessTokenName;
	}

	public SSOConfig setCookieName(String cookieName) {
		this.cookieName = cookieName;
		return this;
	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public SSOConfig setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
		return this;
	}

	public String getCookiePath() {
		return cookiePath;
	}

	public SSOConfig setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
		return this;
	}

	public boolean isCookieSecure() {
		return cookieSecure;
	}

	public SSOConfig setCookieSecure(boolean cookieSecure) {
		this.cookieSecure = cookieSecure;
		return this;
	}

	public boolean isCookieHttponly() {
		return cookieHttponly;
	}

	public SSOConfig setCookieHttponly(boolean cookieHttponly) {
		this.cookieHttponly = cookieHttponly;
		return this;
	}

	public int getCookieMaxage() {
		return cookieMaxage;
	}

	public SSOConfig setCookieMaxage(int cookieMaxage) {
		this.cookieMaxage = cookieMaxage;
		return this;
	}

	public boolean isCookieBrowser() {
		return cookieBrowser;
	}

	public SSOConfig setCookieBrowser(boolean cookieBrowser) {
		this.cookieBrowser = cookieBrowser;
		return this;
	}

	public boolean isCookieCheckip() {
		return cookieCheckip;
	}

	public SSOConfig setCookieCheckip(boolean cookieCheckip) {
		this.cookieCheckip = cookieCheckip;
		return this;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public SSOConfig setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
		return this;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public SSOConfig setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
		return this;
	}

	public String getParamReturl() {
		return paramReturl;
	}

	public SSOConfig setParamReturl(String paramReturl) {
		this.paramReturl = paramReturl;
		return this;
	}

	public long getCacheExpires() {
		return cacheExpires;
	}

	public SSOConfig setCacheExpires(long cacheExpires) {
		this.cacheExpires = cacheExpires;
		return this;
	}

	public boolean isPermissionUri() {
		return permissionUri;
	}

	public SSOConfig setPermissionUri(boolean permissionUri) {
		this.permissionUri = permissionUri;
		return this;
	}

	public SSOToken getSsoToken() {
		return ssoToken;
	}

	public void setSsoToken(SSOToken ssoToken) {
		this.ssoToken = ssoToken;
	}

	public List<SSOPlugin> getPluginList() {
		return pluginList;
	}

	public SSOConfig setPluginList(List<SSOPlugin> pluginList) {
		this.pluginList = pluginList;
		return this;
	}

	public SSOCache getCache() {
		return cache;
	}

	public SSOConfig setCache(SSOCache cache) {
		this.cache = cache;
		return this;
	}

	public SSOAuthorization getAuthorization() {
		return authorization;
	}

	public static void setAuthorization(SSOAuthorization ssoAuthorization) {
		SSOConfig.authorization = ssoAuthorization;
	}

	/**
	 * <p>
	 * 生成 Token 缓存主键
	 * </p>
	 *
	 * @param userId
	 *            用户ID
	 * @param appType
	 *            用户ID
	 * @return
	 */
	public String toCacheKey(HttpServletRequest req, String uid, AppTypeEnum appType) {
		if (reidsKeyPrefix != null) {
			reidsKeyPrefix = reidsKeyPrefix.toUpperCase();
			reidsKeyPrefix = reidsKeyPrefix.replace("SSO_", "SSO:");
		} else {
			reidsKeyPrefix = "SSO:";
		}
		if (reidsKeyPrefix != null && !reidsKeyPrefix.toUpperCase().startsWith("SSO:")) {
			reidsKeyPrefix = "SSO:" + reidsKeyPrefix;
		}

		/*
		 * String userAgent = req.getHeader("User-Agent"); if
		 * (reidsKeyPrefix.indexOf("WX_")<0){ if
		 * (userAgent.toLowerCase().indexOf("miniprogram")>-1){
		 * reidsKeyPrefix+="WX_"; }else
		 * if(userAgent.toLowerCase().indexOf("micromessenger")>-1){
		 * reidsKeyPrefix+="WX_"; } }
		 */

		String id = "";
		StringBuffer ck = new StringBuffer(reidsKeyPrefix);

		/**
		 * Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv)
		 * AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0
		 * Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043807 Mobile Safari/537.36
		 * MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN
		 * Mozilla/5.0 (Linux; Android 7.1.1; OD103 Build/NMF26F; wv)
		 * AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0
		 * Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043632 Safari/537.36
		 * MicroMessenger/6.6.1.1220(0x26060135) NetType/4G Language/zh_CN
		 * Mozilla/5.0 (Linux; Android 6.0.1; SM919 Build/MXB48T; wv)
		 * AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0
		 * Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043632 Safari/537.36
		 * MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN
		 * Mozilla/5.0 (Linux; Android 5.1.1; vivo X6S A Build/LMY47V; wv)
		 * AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0
		 * Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043632 Safari/537.36
		 * MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN
		 * Mozilla/5.0 (Linux; Android 5.1; HUAWEI TAG-AL00
		 * Build/HUAWEITAG-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko)
		 * Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043622
		 * Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/4G
		 * Language/zh_CN
		 */
		String userAgent = req.getHeader("User-Agent");
		/*
		 * if (userAgent.matches("/Android/") &&
		 * userAgent.matches("/miniProgram/")){ ck.append("WX_"); }else
		 * if(userAgent.matches("/iPhone/") &&
		 * userAgent.matches("/MicroMessenger/")){ ck.append("WX_"); }
		 */
		ck.append(appType.getValue() + "_" + appType.getCode()+"_");
		ck.append("Token_" + uid);
		return ck.toString().toUpperCase();
	}

	/**
	 * 得到用户帐号的缓存key
	 * 
	 * @param <T>
	 * @param req
	 * @param uid
	 * @param c
	 * @return
	 */
	public <T> String toCacheKeyForUserInfo(HttpServletRequest req, String uid, Class<T> c) {
		if (reidsKeyPrefix != null && !reidsKeyPrefix.toUpperCase().startsWith("SSO")) {
			reidsKeyPrefix = "SSO_" + reidsKeyPrefix;
		} else if (reidsKeyPrefix == null) {
			reidsKeyPrefix = "SSO_";
		}
		StringBuffer ck = new StringBuffer(reidsKeyPrefix);
		ck.append("USERINFO_");
		ck.append(c.getSimpleName() + "_");
		ck.append(uid);
		return ck.toString();
	}

	public boolean isCacheEnable() {
		return cacheEnable;
	}

	public void setCacheEnable(boolean cacheEnable) {
		this.cacheEnable = cacheEnable;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getOverUrl() {
		return overUrl;
	}

	public void setOverUrl(String overUrl) {
		this.overUrl = overUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getAuthServiceUrl() {
		return authServiceUrl;
	}

	public void setAuthServiceUrl(String authServiceUrl) {
		this.authServiceUrl = authServiceUrl;
	}

	public boolean isAjaxEnable() {
		return ajaxEnable;
	}

	public void setAjaxEnable(boolean ajaxEnable) {
		this.ajaxEnable = ajaxEnable;
	}

	public boolean isRedisEnable() {
		return redisEnable;
	}

	public void setRedisEnable(boolean redisEnable) {
		this.redisEnable = redisEnable;
	}

	public String getReidsKeyPrefix() {
		return reidsKeyPrefix;
	}

	public void setReidsKeyPrefix(String reidsKeyPrefix) {
		this.reidsKeyPrefix = reidsKeyPrefix;
	}

	public String getAccessUserId() {
		return accessUserId;
	}

	public void setAccessUserId(String accessUserId) {
		this.accessUserId = accessUserId;
	}

	/*
	 * public static ApplicationContext getApplicationContext() { return
	 * applicationContext; }
	 * 
	 * public static void setApplicationContext(ApplicationContext
	 * applicationContext) { SSOConfig.applicationContext = applicationContext;
	 * }
	 */

	public String getUserInfoServiceUrl() {
		return userInfoServiceUrl;
	}

	public void setUserInfoServiceUrl(String userInfoServiceUrl) {
		this.userInfoServiceUrl = userInfoServiceUrl;
	}

	public static RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public static void setRestTemplate(RestTemplate restTemplate) {
		SSOConfig.restTemplate = restTemplate;
	}

/*	public static void main(String[] arg) {
		System.out.print("System.currentTimeMillis() + 1000 * 60 * 1440=" + 60 * 1440);
		
		String android = "Android.*MicroMessenger.*miniProgram";
		//String apple = "MicroMessenger.*";
		String regex = ".*MicroMessenger.*";
		Pattern pattern = Pattern.compile(regex);
		
		String appRegex = ".*ghyoho.*";
		
		String ua1 = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_1_1 like Mac OS X) AppleWebKit/604.3.5 (KHTML, like Gecko) Mobile/15B150 MicroMessenger/6.6.1 NetType/WIFI Language/zh_CN";
		String ua2 = "ghyoho_worker_android Mozilla/5.0 (Linux; Android 5.1.1; vivo X6S A Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043632 Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN";
		
		String ua3 = "Mozilla/5.0 (Linux; Android 5.1.1; vivo X6S A Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043632 Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN";

		System.out.println("userAgent1="+Pattern.matches(appRegex,ua1));
		System.out.println("userAgent2="+Pattern.matches(appRegex,ua2));
		System.out.println("userAgent3="+Pattern.matches(appRegex,ua3));
	}*/

}
