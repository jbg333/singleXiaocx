package com.weixin.note.serv.sso.token;

import java.util.Date;

import com.weixin.note.serv.sso.config.SSOConfig;
import com.weixin.note.serv.sso.jwt.JWTUtils;
import com.weixin.note.serv.sso.jwt.RSAUtils;
import com.weixin.note.serv.sso.token.util.TokenConstants;
import com.weixin.note.serv.sso.token.util.TokenFlag;
import com.weixin.note.serv.sso.token.util.TokenOrigin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * API调用认证工具类，采用RSA加密 1.公钥加密，2.私钥解密
 * 
 * @author yinjihuan
 *
 */
public class SSOJwtUtils extends JWTUtils{
	protected static class SingletonHolder {
		private static final SSOJwtUtils INSTANCE = new SSOJwtUtils();
	}
	
	public synchronized static SSOJwtUtils getInstance() {
		if (priKey == null && pubKey == null) {
			priKey = RSAUtils.getPrivateKey(RSAUtils.modulus, SSOConfig.getInstance().getPrivateKey());
			pubKey = RSAUtils.getPublicKey(RSAUtils.modulus, SSOConfig.getInstance().getPublicKey());
		}
		return SingletonHolder.INSTANCE;
	}
	
	 

	public String getToken(SSOToken jwtToken) {
		long endTime = System.currentTimeMillis() + 1000 * 60 * 1440;
		
		
		return getToken(jwtToken,endTime);
	}
	
	
	/**
	 * SSOConstants
	 * @param jwtToken
	 * @param exp
	 * @param priKey:单位分钟
	 * @return
	 */
	public String getToken(SSOToken jwtToken,long exp) {
		long endTime = System.currentTimeMillis() + 1000 * 60 * exp;
		return Jwts.builder()
				.setSubject(jwtToken.getId())
				.setId(jwtToken.getId())
				.claim(TokenConstants.TOKEN_USER_AGENT, jwtToken.getUserAgent())
				.claim(TokenConstants.TOKEN_USER_IP, jwtToken.getIp())
				.claim(TokenConstants.TOKEN_ORIGIN, jwtToken.getOrigin())
				.claim(TokenConstants.TOKEN_USERNAME, jwtToken.getUsername())
				.claim(TokenConstants.TOKEN_APPTYPE, jwtToken.getAppType())
				.setIssuer(jwtToken.getIssuer())
				.setIssuedAt(jwtToken.getTime())
				.setExpiration(new Date(endTime))
				.signWith(SignatureAlgorithm.RS512, priKey)
				.compact();
	}

		
	
	
	/**
	 * 
	 * @param token
	 * @param header
	 * @return
	 */
	public SSOToken parserById(String uid, boolean header) {
		Claims claims  = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(uid).getBody();
		if (null == claims) {
            return null;
        }
		
		
		SSOToken jwtToken = new SSOToken();
		jwtToken.setId(claims.getId());
		jwtToken.setIssuer(claims.getIssuer());
        Object ip = claims.get(TokenConstants.TOKEN_USER_IP);
        if (null != ip) {
        	jwtToken.setIp(String.valueOf(ip));
        }
        Object userAgent = claims.get(TokenConstants.TOKEN_USER_AGENT);
        if (null != userAgent) {
        	jwtToken.setUserAgent(String.valueOf(userAgent));
        }
        
        Object origin = claims.get(TokenConstants.TOKEN_ORIGIN);
        if (header && null == origin) {
            return null;
        }
        
        // TOKEN 来源
        if (null != origin) {
        	jwtToken.setOrigin(TokenOrigin.fromValue(String.valueOf(origin)));
        }
        
        
        Object tokenFlag = claims.get(TokenConstants.TOKEN_FLAG);
        if (null != tokenFlag) {
        	jwtToken.setFlag(TokenFlag.fromValue(String.valueOf(tokenFlag)));
        }
        
        Object username = claims.get(TokenConstants.TOKEN_USERNAME);
        if (null != username) {
        	jwtToken.setUsername(String.valueOf(username));
        }
        
        Object appType = claims.get(TokenConstants.TOKEN_APPTYPE);
        if (null != username) {
        	jwtToken.setAppType(String.valueOf(appType));
        }
        
        jwtToken.setTime(claims.getIssuedAt());
        
        
        Date curDate = new Date();
        boolean res = curDate.after(claims.getExpiration());
        if (res){
        	return null;
        }
        return jwtToken;
    }
	
	
	/**
	 * 
	 * @param token
	 * @param header
	 * @return
	 */
	public SSOToken parser(String token, boolean header) {
		Claims claims  = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token).getBody();
		if (null == claims) {
            return null;
        }
		SSOToken jwtToken = new SSOToken();
		jwtToken.setId(claims.getId());
		jwtToken.setIssuer(claims.getIssuer());
        Object ip = claims.get(TokenConstants.TOKEN_USER_IP);
        if (null != ip) {
        	jwtToken.setIp(String.valueOf(ip));
        }
        Object userAgent = claims.get(TokenConstants.TOKEN_USER_AGENT);
        if (null != userAgent) {
        	jwtToken.setUserAgent(String.valueOf(userAgent));
        }
        
        Object origin = claims.get(TokenConstants.TOKEN_ORIGIN);
        if (header && null == origin) {
            return null;
        }
        
        // TOKEN 来源
        if (null != origin) {
        	jwtToken.setOrigin(TokenOrigin.fromValue(String.valueOf(origin)));
        }
        
        
        Object username = claims.get(TokenConstants.TOKEN_USERNAME);
        if (null != username) {
        	jwtToken.setUsername(username.toString());
        }
        
        Object appType = claims.get(TokenConstants.TOKEN_APPTYPE);
        if (null != appType) {
        	jwtToken.setAppType(appType.toString());
        }
        
        
        jwtToken.setTime(claims.getIssuedAt());        
        jwtToken.setToken(token);
        
        Date curDate = new Date();
        boolean res = curDate.after(claims.getExpiration());
        if (res){
        	return null;
        }
        return jwtToken;
    }
	
	
	public final static void testparser(String token, boolean header) {
		//String token="eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiIxIiwianRpIjoiMSIsInVhIjoiZGJlMGMiLCJpcCI6IjA6MDowOjA6MDowOjA6MSIsIm9nIjoiQ09PS0lFIiwidW5hbWUiOiJhZG1pbiIsImlzcyI6IjE4NjAxMTA2MzMzQDE2My5jb20iLCJpYXQiOjE1MjI1OTYyMTcsImV4cCI6OTEzNjI0Nzk2NTEzMTd9.IbJLCsGsWTv0FjqJcQPTUpnJ2uTRLjF_Hf3ZHdeje8pSnHnlgrWCIs8aOsPpQWaH41WVFmKzoBBkko1ZWwInju71v2FUE-QpUck8407qn-uZVN0Xdk9OLYDczKF2-LSSI-xIswvaArvkkCVzEY7yxFNhu-hSQOJKjrKrCWKr3SU";
		System.out.println(Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token).toString());
		System.out.println(Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token).getBody().toString());
    }
	
	
	public static void main(String[] arg){
		String token="eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiIxIiwianRpIjoiMSIsInVhIjoiZGJlMGMiLCJpcCI6IjA6MDowOjA6MDowOjA6MSIsIm9nIjoiQ09PS0lFIiwidW5hbWUiOiJhZG1pbiIsImlzcyI6IjE4NjAxMTA2MzMzQDE2My5jb20iLCJpYXQiOjE1MjI1OTYyMTcsImV4cCI6OTEzNjI0Nzk2NTEzMTd9.IbJLCsGsWTv0FjqJcQPTUpnJ2uTRLjF_Hf3ZHdeje8pSnHnlgrWCIs8aOsPpQWaH41WVFmKzoBBkko1ZWwInju71v2FUE-QpUck8407qn-uZVN0Xdk9OLYDczKF2-LSSI-xIswvaArvkkCVzEY7yxFNhu-hSQOJKjrKrCWKr3SU";
		SSOJwtUtils.getInstance().testparser(token, false);
	}

}
