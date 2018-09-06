/**  
 * Project Name:single-wx-service  
 * File Name:SSOConfigure.java  
 * Package Name:com.weixin.note.serv.config  
 * Date:2018年9月6日下午1:38:58  
 * Copyright (c) 2018, jbg@126.com All Rights Reserved.  
 *  
*/  
  
package com.weixin.note.serv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.weixin.note.serv.sso.SSOAuthorization;
import com.weixin.note.serv.sso.config.SSOConfig;
import com.weixin.note.serv.sso.starter.SSOProperties;
import com.weixin.note.serv.util.SpringContextUtils;

/**  
 * ClassName:SSOConfigure   
 * Date:     2018年9月6日 下午1:38:58   
 * @author   jbg  
 * @version    
 * @since    JDK 1.8  
 * @see        
 */
@Configuration
//@EnableConfigurationProperties(SSOProperties.class)
public class SSOConfigure {
	@Autowired
	public SSOProperties ssoProperties;
/*	@Bean
	public SSOProperties ssoProperties() {
		return new SSOProperties();
	}*/
	@Autowired
	public RestTemplate restTemplate;
	
	/**
	 * 注入初始化
	 */
	@Bean
	public SSOConfig getSSOConfig() {
		SSOConfig.setRestTemplate(restTemplate);		
		SSOAuthorization ssoAuthorization = (SSOAuthorization) SpringContextUtils.getBean("ssoAuthorization");
		SSOConfig.setAuthorization(ssoAuthorization);		
		return SSOConfig.init(ssoProperties.getConfig());
	}
	
}
  
