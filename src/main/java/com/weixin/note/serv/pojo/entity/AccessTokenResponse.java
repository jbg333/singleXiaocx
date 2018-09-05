package com.weixin.note.serv.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessTokenResponse extends BaseError {
	
	/**
	 * 获取到的凭证
	 */
	@JsonProperty("access_token")
	private String accessToken;
	
	/**
	 * 凭证有效时间，单位：秒
	 */
	@JsonProperty("expires_in")
	private Integer expiresIn;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	
	
	
	
}
