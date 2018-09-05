package com.weixin.note.serv.redis.key;


import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;



public class KeyUtils {	
	protected final static Logger logger = LoggerFactory.getLogger(KeyUtils.class);
	public static final long COUNT_EXPIRE =  60;		
    //------------------------------------过期时间设置----------------------------
	public static final int FIVE_SEC	=5; 		//5秒
	public static final int FIVE_MIN	=300; 	//5分钟  
	public static final int QUARTY_HOUR	=900; 	//15分钟
	public static final int HALF_HOUR	=1800;
	public static final int ONE_HOUR	=3600;  //1小时
    public static final int NEVER_EXPIRE =0;    //永不过期
    
	public static final String bytag="@{";
	public static final String exptag="@EXP{";
	
	/**
	 * 无条件
	 * @param prefix
	 * @param type
	 * @param keyword
	 * @return
	 */
	public static String constructKey(String prefix, ValueTypeEnum type,String keyword){
		return constructKey(prefix,type,keyword,null);
	}
	
	public static String constructKey(String prefix, String keyword){
		return constructKey(prefix,null,keyword,null);
	}
	
	
	public static String constructKey(String prefix, ValueTypeEnum type,String keyword,long second){
		return constructKey(prefix,type,keyword,null,second);
		
	}	public static String constructKey(String prefix, String keyword,long second){
		return constructKey(prefix,null,keyword,null,second);
	}
	
	
	
	/**
	 * 有条件
	 * @param prefix
	 * @param type
	 * @param keyword
	 * @param suffix
	 * @return
	 */
	public static String constructKey(String prefix, ValueTypeEnum type,String keyword,String[] suffix){
		return constructKey(prefix,type,keyword,suffix,null);
	}
	
	
//	public static String constructKey(String prefix, ValueTypeEnum type,String keyword,Long second){
//		String key = constructKey(prefix,type,keyword,null,second);
//		return key;
//	}
//	
	
	/**
	 * 无枚举类型
	 * @param prefix
	 * @param keyword
	 * @param suffix
	 * @param second
	 * @return
	 */
	public static String constructKey(String prefix, String keyword,String[] suffix,Long second){
		return constructKey( prefix, null, keyword, suffix,second);
	}
	
	/**
	 * 无枚举类型
	 * @param prefix
	 * @param keyword
	 * @param suffix
	 * @param second
	 * @return
	 */
	public static String constructKey(String prefix, String keyword,String[] suffix){
		return constructKey( prefix, null, keyword, suffix);
	}
	 
	
	/**
	 * 有条件
	 * @param prefix
	 * @param type
	 * @param keyword
	 * @param suffix
	 * @param expire 过期时间
	 * @return
	 */
	public static String constructKey(String prefix, ValueTypeEnum type,String keyword,String[] suffix,Long second){
		if (StringUtils.isEmpty(prefix)){
			throw new RuntimeException("前缀不可以为空且格式必须是");
		}
		if (StringUtils.isEmpty(keyword)){
			throw new RuntimeException("关键字不可以为空，且必须保证唯一");
		}
		
		if (prefix.lastIndexOf("_")>-1){
			prefix = prefix.substring(0,prefix.length()-1);
		}
		if (prefix.lastIndexOf(":")!=0){
			prefix = prefix+":";
		}
		StringBuffer key = new StringBuffer(prefix);	
		if (type!=null){
			key.append(type.name());
			if (keyword.indexOf("_")<=-1){
				key.append("_");
			}
		}
		key.append(keyword);
		
		//String bytag = "@BY{";
		if (key.toString().toUpperCase().contains(bytag)){
			String tmp = key.toString().toUpperCase();
			tmp = tmp.replace(bytag, "%");
			key = new StringBuffer(tmp);
		}
		
		if (suffix!=null && suffix.length>=0) {
			JSONObject jsonObject = new JSONObject(suffix.length,true);		
			for (int i=0;i<suffix.length;i++){
				jsonObject.put(suffix[i], "?");
			}
			String json = jsonObject.toJSONString();
			key.append(bytag.substring(0, bytag.length()-1));
			key.append(json);
		}
		
		/**
		 * 如果有过期时间
		 */		
		//String exptag = "@EXP{";
		if (key.toString().toUpperCase().contains(exptag)){
			String tmp = key.toString().toUpperCase();
			tmp = tmp.replace(exptag, "_EXP_");
			key = new StringBuffer(tmp);
		}
		if (second!=null && second!=-1){
			key.append(exptag+second+"}");
		}		
		
		return key.toString().toUpperCase();
	}
	
	
	public static long getExpire(String key){
		//String tag = "@EXP{";
		if (key.indexOf(exptag)>-1){
			String endtmp = key.substring(key.indexOf(exptag)+exptag.length());
			String v = endtmp.substring(0,endtmp.indexOf("}"));
			return Long.valueOf(v);
		}else{
			return -1;
		}
	}
	
	public static String getPrefix(String key){
		String prefix = key;
		if (key.indexOf(bytag)>-1){
			int end = key.indexOf(bytag);
			prefix = key.substring(0, end);
		}
		if (prefix.indexOf(exptag)>-1){
			prefix = prefix.substring(0,prefix.indexOf(exptag));
		}
		return prefix;
		
	}
	
	public static String generateKey(String oriKey){
		return generateKey(oriKey,null);
	}
	
	/**
	 * {"message":"ok","value":"9","code":"11001488","success":true}
	 * @param oriKey
	 * @param suffixValue
	 * @return
	 */
	public static String generateKey(String oriKey,String[] value){
		int end = oriKey.length()-1;
		String prefix = oriKey;
		if (oriKey.indexOf(bytag)>-1){
			end = oriKey.indexOf(bytag);
			prefix = oriKey.substring(0, end);
		}else{
			return clean(prefix.toString().toUpperCase());
		}
		
		StringBuffer key = new StringBuffer(prefix);
		
		
		String json = oriKey.substring(oriKey.indexOf(bytag)+bytag.length()-1);
		json = json.substring(0,json.indexOf("}")+1);
		
		JSONObject jsonObject = JSONObject.parseObject(json,Feature.OrderedField);
		if (value==null || jsonObject.size()!=value.length){
			throw new RuntimeException("参数为空或总数不匹配!");
		}
		
		Iterator<String> keys = jsonObject.keySet().iterator();		
		StringBuffer v = new StringBuffer("");
		int i=0;
		while(keys.hasNext()){
			String k = keys.next().toString();
			v.append(","+k+"="+value[i++]);
		}
		key.append(bytag);
		key.append(v.toString().substring(1));
		key.append("}");
		
		if (oriKey.indexOf(exptag)>-1){
			key.append(oriKey.substring(oriKey.indexOf(exptag)));
		}
		
		return clean(key.toString().toUpperCase());
	}
	
	/**
	 * 清理特殊字符
	 * @return
	 */
	private static String clean(String key){
		key = key.replace("_@", "@");
		key = key.replace("_:", ":");
		key = key.replace("::", ":");
		key = key.replace(":_", ":");	
		key = key.replaceAll("\"", "");
		key = key.replaceAll("\\\\", "");
		return key;
	}
	
/*	public static String generateKey(String oriKey,String value){
		return generateKey(oriKey,new String[]{value});
	}
	*/
	
	 
	
	public static void main(String[] args) {
//		String key = KeyUtils.generateKey(RedisConstant.KEY_EXAM_TIMESTAMP, "1");
		String KEY_SERVICE_AREA= KeyUtils.constructKey("abc",ValueTypeEnum.BO , "_serviceArea",new String[]{"a","b","c"},120l);
		System.out.println(KEY_SERVICE_AREA);
		String key = KeyUtils.generateKey(KEY_SERVICE_AREA, new String[]{"3","1","?"});
		System.out.println(key);
		System.out.println(KeyUtils.getExpire(key));
		System.out.println(KeyUtils.getPrefix(key));
	}
	
	
	
	
}