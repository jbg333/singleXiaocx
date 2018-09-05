/**  
 * Project Name:pojo-base  
 * File Name:DataStatusEnum.java  
 * Package Name:com.gh.web.base.enumresource  
 * Date:2018年8月1日上午11:58:43  
 * Copyright (c) 2018, jbg@126.com All Rights Reserved.  
 *  
*/  
  
package com.weixin.note.serv.pojo.enm;

/**  
 * ClassName:DataStatusEnum   
 * Date:     2018年8月1日 上午11:58:43   
 * @author   jbg  
 * @version    
 * @since    JDK 1.8  
 * @see        逻辑删除使用
 */
public enum DataStatusEnum implements EnumMessage{
		A("A","新建"),
		E("E","有效"),
		D("D","已删除");
	 private final String code;
	 private final String value;
	 private DataStatusEnum(String code, String value) {
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
  
