package com.weixin.note.serv.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录后首页服务
 * @author lenovo
 *
 */
public interface ILoginPageService {
	
	/**
	 * 得到登陆用的token 并缓存
	 * getUserLoginStat:(). 
	 * @author jbg  
	 * @param loginCode
	 * @param request
	 * @return  
	 * @since JDK 1.8
	 */
	public String getUserLoginStat(String loginCode,HttpServletRequest request);
}
