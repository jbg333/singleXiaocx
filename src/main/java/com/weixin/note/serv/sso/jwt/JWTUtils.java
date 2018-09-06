package com.weixin.note.serv.sso.jwt;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * 
 * API调用认证工具类，采用RSA加密 1.公钥加密，2.私钥解密
 * 
 * @author yinjihuan
 *
 */
public class JWTUtils {
	protected static RSAPrivateKey priKey;
	protected static RSAPublicKey pubKey;

	protected static class SingletonHolder {
		private static final JWTUtils INSTANCE = new JWTUtils();
	}

	public synchronized static JWTUtils getInstance(String modulus, String privateExponent, String publicExponent) {
		if (priKey == null && pubKey == null) {
			priKey = RSAUtils.getPrivateKey(modulus, privateExponent);
			pubKey = RSAUtils.getPublicKey(modulus, publicExponent);
		}
		return SingletonHolder.INSTANCE;
	}

	public synchronized static void reload(String modulus, String privateExponent, String publicExponent) {
		priKey = RSAUtils.getPrivateKey(modulus, privateExponent);
		pubKey = RSAUtils.getPublicKey(modulus, publicExponent);
	}

	public synchronized static JWTUtils getInstance() {
		if (priKey == null && pubKey == null) {
			priKey = RSAUtils.getPrivateKey(RSAUtils.modulus, RSAUtils.private_exponent);
			pubKey = RSAUtils.getPublicKey(RSAUtils.modulus, RSAUtils.public_exponent);
		}
		return SingletonHolder.INSTANCE;
	}

	
	/**
	 * 根据appId生成token，用户客户端接入校验
	 * @param appId
	 * @param exp
	 * @return
	 */
	public String getClientAccessToken(String clientAppId) {
		long endTime = System.currentTimeMillis() + 1000 * 60 * 1440;
		return Jwts.builder().setSubject(clientAppId).setExpiration(new Date(endTime))
				.signWith(SignatureAlgorithm.RS512, priKey)
				.compact();
	}

	
	/**
	 * 获取Token,私钥生成token
	 * 
	 * @param uid
	 *            用户ID
	 * @param exp
	 *            失效时间，单位分钟
	 * @return
	 */
	public String getToken(String uid, int exp) {
		long endTime = System.currentTimeMillis() + 1000 * 60 * exp;
		return getToken(uid,endTime,priKey);
	}
	
	

	/**
	 * 获取Token,,私钥生成token,过期时间24小时。
	 * 
	 * @param uid
	 *            用户ID
	 * @return
	 */
	public String getToken(String uid) {
		long endTime = System.currentTimeMillis() + 1000 * 60 * 1440;
		return getToken(uid,endTime,priKey);
	}
	
	public String getToken(String uid,long exp,RSAPrivateKey priKey) {
		long endTime = System.currentTimeMillis() + 1000 * 60 * exp;
		return Jwts.builder().setSubject(uid).setExpiration(new Date(endTime))
				.signWith(SignatureAlgorithm.RS512, priKey).compact();
	}


	
	/**
	 * Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token).getBody().setExpiration(exp)
	 * @param token
	 * @return
	 */
	public void clearToken(String token){
		try {
			Claims claims = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token).getBody();
			//claims.setIssuedAt(new Date());
			claims.setExpiration(new Date());
			claims.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String signCompact(JwtBuilder jwtBuilder) {
		return jwtBuilder.signWith(SignatureAlgorithm.RS512, priKey).compact();
	}
	
	/**
	 * 检查Token是否合法,公钥校验。用黰 诉公钥校 验
	 * 
	 * @param token
	 * @return JWTResult
	 */
/*	public JWTResult checkToken(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token).getBody();
			
			String sub = claims.get("sub", String.class);		
			return new JWTResult(true, sub, "合法请求", ResponseCode.SUCCESS_CODE.getCode());
		} catch (ExpiredJwtException e) {
			// 在解析JWT字符串时，如果‘过期时间字段’已经早于当前时间，将会抛出ExpiredJwtException异常，说明本次请求已经失效
			return new JWTResult(false, null, "token已过期", ResponseCode.TOKEN_TIMEOUT_CODE.getCode());
		} catch (SignatureException e) {
			// 在解析JWT字符串时，如果密钥不正确，将会解析失败，抛出SignatureException异常，说明该JWT字符串是伪造的
			return new JWTResult(false, null, "非法请求", ResponseCode.NO_AUTH_CODE.getCode());
		} catch (Exception e) {
			return new JWTResult(false, null, "非法请求", ResponseCode.NO_AUTH_CODE.getCode());
		}
	}*/

	/**
	 * 合法有效
	 */
	public static JWTResult checkToken(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token).getBody();
			String sub = claims.get("sub", String.class);
			return new JWTResult(true, sub, "合法请求", ResponseCode.SUCCESS_CODE.getCode());
		} catch (ExpiredJwtException e) {
			// 在解析JWT字符串时，如果‘过期时间字段’已经早于当前时间，将会抛出ExpiredJwtException异常，说明本次请求已经失效
			return new JWTResult(false, null, "token已过期", ResponseCode.TOKEN_TIMEOUT_CODE.getCode());
		} catch (SignatureException e) {
			// 在解析JWT字符串时，如果密钥不正确，将会解析失败，抛出SignatureException异常，说明该JWT字符串是伪造的
			return new JWTResult(false, null, "非法请求", ResponseCode.NO_AUTH_CODE.getCode());
		} catch (Exception e) {
			return new JWTResult(false, null, "非法请求", ResponseCode.NO_AUTH_CODE.getCode());
		}
	}

 
	/*public static class JWTResult {
		private boolean status;
		private String uid;
		private String msg;
		private int code;

		public JWTResult() {
			super();
		}

		public JWTResult(boolean status, String uid, String msg, int code) {
			super();
			this.status = status;
			this.uid = uid;
			this.msg = msg;
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}
	}*/
}
