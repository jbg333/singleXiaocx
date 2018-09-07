package com.weixin.note.serv.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.weixin.note.serv.pojo.entity.WxSessionResult;
import com.weixin.note.serv.service.ILoginPageService;

public class LoginPageServiceImpl implements ILoginPageService {
	@Autowired
	private RestTemplate restTemplate;
	
	private String getUserLoginStat(String loginCode) {
		String url = "https://api.weixin.qq.com/sns"
				+ "/jscode2session?appid=APPID"
				+ "&secret=SECRET"
				+ "&js_code=#JSCODE"
				+ "&grant_type=authorization_code";
		url = url.replace("#JSCODE", loginCode);
		//请求微信服务 
		ResponseEntity<WxSessionResult> result = restTemplate.getForEntity(url,WxSessionResult.class);
		//进行
		
		
		
		return null;
	}
}
