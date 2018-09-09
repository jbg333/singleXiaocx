package com.weixin.note.serv.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weixin.note.serv.pojo.enm.AppTypeEnum;
import com.weixin.note.serv.pojo.entity.WxSessionResult;
import com.weixin.note.serv.service.ILoginPageService;
import com.weixin.note.serv.sso.SSOHelper;
import com.weixin.note.serv.sso.token.SSOToken;



@Service
public class LoginPageServiceImpl implements ILoginPageService {
	@Autowired
	private RestTemplate restTemplate;
	
	
	public String getUserLoginStat(String loginCode,HttpServletRequest request) {
		String url = "https://api.weixin.qq.com/sns"
				+ "/jscode2session?appid=wx00e5d810c8e6f47e"
				+ "&secret=c6d24164daaf9a8086f8ff8ad2d63f49"
				+ "&js_code=#JSCODE"
				+ "&grant_type=authorization_code";
		url = url.replace("#JSCODE", loginCode);
		//请求微信服务 
		WxSessionResult wxRet = restTemplate.getForObject(url,WxSessionResult.class);
		//进行
		//WxSessionResult wxRet = result.getBody();
		SSOToken ssoToken = new SSOToken();
		ssoToken.setId(wxRet.getOpenid());			
		ssoToken.setAppType(AppTypeEnum.WX_NOTE.getCode());
		ssoToken.setTime(new Date());
		ssoToken.setWxSessionKey(wxRet.getSession_key());			
		String token = SSOHelper.setToken(ssoToken, request);
		return token;
	}
}
