package com.weixin.note.serv.sso.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * HTTP工具类
 * </p>
 *
 * @author hubin
 * @since 2017-07-17
 */
public class HttpUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	public static final String XML_HTTP_REQUEST = "XMLHttpRequest";
	public static final String X_REQUESTED_WITH = "X-Requested-With";
	public static final String ENCODE = "UTF-8";

	/**
	 * 得到请求头的信息
	 * @param request
	 * @return
	 */
	public static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
     }
	
	/**
	 * <p>
	 * 判断请求是否为 AJAX
	 * </p>
	 *
	 * @param request
	 *            当前请求
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request) {
		return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
	}

	/**
	 * <p>
	 * AJAX 设置 response 返回状态
	 * </p>
	 *
	 * @param response
	 * @param status
	 *            HTTP 状态码
	 * @param tip
	 */
	public static void ajaxStatus(HttpServletResponse response, int status, String tip) {
		try {
			response.setContentType("text/html;charset=UTF-8" + ENCODE);
			response.setStatus(status);
			PrintWriter out = response.getWriter();
			out.print(tip);
			out.flush();
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}

	/**
	 * <p>
	 * 获取当前 URL 包含查询条件
	 * </p>
	 *
	 * @param request
	 * @param encode
	 *            URLEncoder编码格式
	 * @return
	 * @throws IOException
	 */
	public static String getQueryString(HttpServletRequest request, String encode) throws IOException {
		StringBuffer sb = new StringBuffer(request.getRequestURL());
		String query = request.getQueryString();
		if (query != null && query.length() > 0) {
			sb.append("?").append(query);
		}
		return URLEncoder.encode(sb.toString(), encode);
	}

	/**
	 * <p>
	 * getRequestURL是否包含在URL之内
	 * </p>
	 *
	 * @param request
	 * @param url
	 *            参数为以';'分割的URL字符串
	 * @return
	 */
	public static boolean inContainURL(HttpServletRequest request, String url) {
		if (url == null && "".equals(url.trim())) {
			return true;
		}
		boolean result = false;

		
		StringBuffer reqUrl = new StringBuffer(request.getRequestURL());
		Pattern imagePerttner = Pattern.compile("(.jpg|.gif|.html|.gif|.css|.js|.mp4|.svg|swagger*)");
		Matcher staticRes = imagePerttner.matcher(reqUrl);
		if (staticRes.find()) {
			return true;
		}
		String[] urlArr = url.split(";");
		for (int i = 0; i < urlArr.length; i++) {
			try {
				String regEx = urlArr[i].replace("/**/", "*");
				regEx = regEx.replace("**", "*");
				regEx = regEx.replace("//", "/");
				Pattern pattern = Pattern.compile(regEx);
				Matcher matcher = pattern.matcher(reqUrl);
				if (matcher.find()) {
					result = true;
					break;
				}
			} catch (Exception e) {

			}
		}
		return result;
	}

	/**
	 * <p>
	 * URLEncoder 返回地址
	 * </p>
	 *
	 * @param url
	 *            跳转地址
	 * @param retParam
	 *            返回地址参数名
	 * @param retUrl
	 *            返回地址
	 * @return
	 */
	public static String encodeRetURL(String url, String retParam, String retUrl) {
		return encodeRetURL(url, retParam, retUrl, null);
	}

	/**
	 * <p>
	 * URLEncoder 返回地址
	 * </p>
	 *
	 * @param url
	 *            跳转地址
	 * @param retParam
	 *            返回地址参数名
	 * @param retUrl
	 *            返回地址
	 * @param data
	 *            携带参数
	 * @return
	 */
	public static String encodeRetURL(String url, String retParam, String retUrl, Map<String, String> data) {
		if (url == null) {
			return null;
		}

		StringBuffer retStr = new StringBuffer(url);
		retStr.append("?");
		retStr.append(retParam);
		retStr.append("=");
		try {
			retStr.append(URLEncoder.encode(retUrl, ENCODE));
		} catch (UnsupportedEncodingException e) {
			logger.error("encodeRetURL error.{}", url);
			e.printStackTrace();
		}

		if (data != null) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				retStr.append("&").append(entry.getKey()).append("=").append(entry.getValue());
			}
		}

		return retStr.toString();
	}

	/**
	 * <p>
	 * URLDecoder 解码地址
	 * </p>
	 *
	 * @param url
	 *            解码地址
	 * @return
	 */
	public static String decodeURL(String url) {
		if (url == null) {
			return null;
		}
		String retUrl = "";

		try {
			retUrl = URLDecoder.decode(url, ENCODE);
		} catch (UnsupportedEncodingException e) {
			logger.error("encodeRetURL error.{} ,{}", url, e.getMessage());
		}

		return retUrl;
	}

	/**
	 * <p>
	 * GET 请求
	 * </p>
	 *
	 * @param request
	 * @return boolean
	 */
	public static boolean isGet(HttpServletRequest request) {
		if ("GET".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * POST 请求
	 * </p>
	 *
	 * @param request
	 * @return boolean
	 */
	public static boolean isPost(HttpServletRequest request) {
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * 请求重定向至地址 location
	 * </p>
	 *
	 * @param response
	 *            请求响应
	 * @param location
	 *            重定向至地址
	 */
	public static void sendRedirect(HttpServletResponse response, String location) {
		try {
			response.sendRedirect(location);
		} catch (IOException e) {
			logger.error("sendRedirect location:{} ,{}", location, e.getMessage());
		}
	}

	/**
	 * <p>
	 * 获取Request Playload 内容
	 * </p>
	 *
	 * @param request
	 * @return Request Playload 内容
	 */
	public static String requestPlayload(HttpServletRequest request) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * <p>
	 * 获取当前完整请求地址
	 * </p>
	 *
	 * @param request
	 * @return 请求地址
	 */
	public static String getRequestUrl(HttpServletRequest request) {
		StringBuffer url = new StringBuffer(request.getScheme());
		// 请求协议 http,https
		url.append("://");
		url.append(request.getHeader("host"));// 请求服务器
		url.append(request.getRequestURI());// 工程名
		if (request.getQueryString() != null) {
			// 请求参数
			url.append("?").append(request.getQueryString());
		}
		return url.toString();
	}
}
