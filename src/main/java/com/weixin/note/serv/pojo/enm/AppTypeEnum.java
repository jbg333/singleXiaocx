/** 
 * <pre>项目名称:pojo-base 
 * 文件名称:ClientType.java 
 * 包名:com.gh.sso.token 
 * 创建日期:2018年4月13日下午2:20:56 
 * Copyright (c) 2018, yuxy123@gmail.com All Rights Reserved.</pre> 
 */  
package com.weixin.note.serv.pojo.enm;

import com.weixin.note.serv.sso.token.util.AbstractEnum;

/** 
 * <pre>项目名称：pojo-base    
 * 类名称：ClientType    
 * 类描述：版本更新客户端类型枚举
 * 创建人：张朝
 * 创建时间：2018年4月13日 下午2:20:56    
 * 修改人：张朝 
 * 修改时间：2018年4月13日 下午2:20:56    
 * 修改备注：       
 * @version </pre>    
 */
public enum AppTypeEnum implements AbstractEnum{
	
	WX_NOTE("1","IOS");

	private final String code;
	private final String value; 
	private Integer codeValue;
	
	private AppTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
        this.codeValue = Integer.parseInt(code);
    }

 
	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return code;
	}
 
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	
	public Integer getCodeValue(){
		return this.codeValue;
	}

}
