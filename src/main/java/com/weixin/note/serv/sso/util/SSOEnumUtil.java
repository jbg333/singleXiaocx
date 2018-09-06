package com.weixin.note.serv.sso.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weixin.note.serv.sso.token.util.AbstractEnum;

/**
 * 枚举工具类
 * @author admin
 * @param <E>
 *
 */
public class SSOEnumUtil {
	
	/**
	 * 根据code得到枚举对象
	 * @param elementType
	 * @param code
	 * @return
	 */
	public static  <T extends Enum<T>> T getEnumByCode(Class<T> elementType, String code){
		List<AbstractEnum> re = toList(elementType);
		for (AbstractEnum v:re){
			if (v.getCode().equals(code)){
				 return Enum.valueOf(elementType,v.toString());
			}
		}
		return null;
		
	}
	
	/**
	 *  得到code和value集的map，用于code和value类型
	 * @param elementType
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String,String> getMap(Class elementType){
		List<AbstractEnum> re = toList(elementType);
		Map<String,String> map = new HashMap<String,String>();
		for (AbstractEnum v:re){
			map.put(v.getCode(), v.getValue());
		}
		return map;
	}	
	
	/**
	 * 根据枚举类，得到列表，一般用于下拉选择
	 * @param elementType
	 * @return
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static <E> List<E> toList(Class elementType) {
    	EnumSet enumSet = EnumSet.allOf(elementType);  
    	List<E> result = new ArrayList();
    	for (Object v:enumSet){
    		result.add((E) Enum.valueOf(elementType, String.valueOf(v)));
    	}
    	return result;    	
    }
    
    
    /**
     * 根据枚举名称得到枚举对象
     * @param elementType
     * @param name
     * @return
     */
    public static  <T extends Enum<T>> T getEnumByName(Class<T> elementType,String name) {
      	 return Enum.valueOf(elementType, name);
    }
    
 
	
	
}
