/**  
 * Project Name:single-wx-service  
 * File Name:DrcrEnum.java  
 * Package Name:com.weixin.note.serv.pojo.enm  
 * Date:2018年9月10日下午6:34:02  
 * Copyright (c) 2018, jbg@126.com All Rights Reserved.  
 *  
*/  
  
package com.weixin.note.serv.pojo.enm;  
/**  
 * ClassName:DrcrEnum   
 * Date:     2018年9月10日 下午6:34:02   
 * @author   jbg  
 * @version    
 * @since    JDK 1.8  
 * @see        
 */
public enum DrcrEnum implements EnumMessage{
	IN("1","借出"),
	OUT("2","借入");
	private final String code;
	private final String value;
	private DrcrEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getValue() {
		return value;
	}

}
  
