
package com.weixin.note.serv.sso.token;

/**
 * <p>
 * JWT 访问票据
 * </p>
 *
 * @author hubin
 * @since 2017-07-17
 */
public class AccessToken {// implements Token {

	protected String token;

	
	public AccessToken() {
		// TO DO NOTHING
	}

	public void setToken(String token) {
		this.token = token;
	}

	
	public String getToken() {
		return this.token;
	}

}
