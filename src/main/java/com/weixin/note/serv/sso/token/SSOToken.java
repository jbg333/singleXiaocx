package com.weixin.note.serv.sso.token;

import java.util.Date;

import com.weixin.note.serv.sso.token.util.TokenFlag;
import com.weixin.note.serv.sso.token.util.TokenOrigin;


public class SSOToken extends AccessToken {
	// TokenOrigin--来对应
	private TokenFlag flag = TokenFlag.NORMAL;
	private TokenOrigin origin = TokenOrigin.COOKIE;
	private String id;
	private String username;
	
	/**
	 * 
	 * 1:IOS
	 * 2:ANDROID
	 * 3:H5
	 * 4:微信小程序
	 * 5:微信公众号
	 * ...
	 * 同"ClientTypeEnum.class"
	 */
	private String appType;  //对应AppTypeEnum's code
	private String issuer;
	private String ip;
	private Date time = new Date(); // 创建日期
	private String userAgent; // 请求头信息	
	private String wxSessionKey;//小程序的sessionKey
	private String uid; //当id=wx_${openId时}，保存workId
	//private long endtime ;
	
	
	
 /*   public String toCacheKey(HttpServletRequest request) {
        return SSOConfig.toCacheKey(this.getId());
    }
*/
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	 

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public TokenOrigin getOrigin() {
		return origin;
	}

	public void setOrigin(TokenOrigin origin) {
		this.origin = origin;
	}

	public TokenFlag getFlag() {
		return flag;
	}

	public void setFlag(TokenFlag flag) {
		this.flag = flag;
	}
	
	/**
	 * 创建新token
	 * @return
	 */
	public String createTokens(){
		token = SSOJwtUtils.getInstance().getToken(this);
		return token;
	}
	
	/**
	 * kisso中的SSOToken.getToken()
	 */
	@Override
	public String getToken() {
		return token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWxSessionKey() {
		return wxSessionKey;
	}

	public void setWxSessionKey(String wxSessionKey) {
		this.wxSessionKey = wxSessionKey;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAppType() {
		return appType;
	}

	
	public void setAppType(String appType) {
		this.appType = appType;
	}

	
	
	
	
	
	
}
