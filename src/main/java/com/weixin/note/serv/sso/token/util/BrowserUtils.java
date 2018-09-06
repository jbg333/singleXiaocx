package com.weixin.note.serv.sso.token.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author  向寒
 *
 */
public class BrowserUtils {
	
	
    
    /**
     * <p>
     * 混淆浏览器版本信息，取 MD5 中间部分字符
     * </p>
     *
     * @param request
     * @return
     * @Description 获取浏览器客户端信息签名值
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = toMD5(request.getHeader("user-agent"));
        if (null == userAgent) {
            return null;
        }
        return userAgent.substring(3, 8);
    }

    /**
     * <p>
     * 请求浏览器是否合法 (只校验客户端信息不校验domain)
     * </p>
     *
     * @param request
     * @param userAgent 浏览器客户端信息
     * @return
     */
    public static boolean isLegalUserAgent(HttpServletRequest request, String userAgent) {
        String ua = getUserAgent(request);
        if (null == ua) {
            return false;
        }
        return ua.equals(userAgent);
    }
    
    
    static String ENCODING = "UTF-8";
    public static final String ALGORITHM = "MD5";
    /**
     * <p>
     * MD5 签名算法
     * </p>
     *
     * @param plainText 需要加密的字符串
     * @return
     * @Description 字符串加密为MD5 中文加密一致通用,必须转码处理： plainText.getBytes("UTF-8")
     */    
    public static String toMD5(String plainText) {
        StringBuffer rlt = new StringBuffer();
        try {
            rlt.append(md5String(plainText.getBytes(ENCODING)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rlt.toString();
    }
    
    public static String md5String(byte[] data) {
        String md5Str = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance(ALGORITHM);
            byte[] buf = md5.digest(data);
            for (int i = 0; i < buf.length; i++) {
                md5Str += Byte2Hex.byte2Hex(buf[i]);
            }
        } catch (Exception e) {
            md5Str = null;
            e.printStackTrace();
        }
        return md5Str;
    }
}
