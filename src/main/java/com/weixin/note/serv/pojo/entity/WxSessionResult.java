/**  
 * Project Name:single-wx-service  
 * File Name:WxSessionResult.java  
 * Package Name:com.weixin.note.serv.pojo.entity  
 * Date:2018年9月7日上午11:36:09  
 * Copyright (c) 2018, jbg@126.com All Rights Reserved.  
 *  
*/  
  
package com.weixin.note.serv.pojo.entity;  
/**  
 * ClassName:WxSessionResult   
 * Date:     2018年9月7日 上午11:36:09   
 * @author   jbg  
 * @version    
 * @since    JDK 1.8  
 * @see        
 */
public class WxSessionResult {
	private String  openid;
	private String session_key;
	private String unionid;
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getSession_key() {
		return session_key;
	}
	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	@Override
	public String toString() {
		return "WxSessionResult [openid=" + openid + ", session_key=" + session_key + ", unionid=" + unionid + "]";
	}
	
	
}
  
