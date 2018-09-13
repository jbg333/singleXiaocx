package com.weixin.note.serv.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 *  WebMvcConfigurationSupport
 * @author admin
 *
 */
@Order(1)
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {// org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		     //registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		     
	 registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
     registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
     registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
     registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
     super.addResourceHandlers(registry);
		
	}
	/**
	 * 拦截器
	 * getSSOInterceptor:(). 
	 * @author jbg  
	 * @return  
	 * @since JDK 1.8
	 */
	@Bean
	public SSOInterceptor ssoInterceptor(){
		SSOInterceptor sso =new SSOInterceptor();
		return sso;
	}
	
	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration addInterceptorLogin = registry.addInterceptor(ssoInterceptor());
		// 排除配置
		// 拦截配置
		addInterceptorLogin.addPathPatterns("/**");
		addInterceptorLogin.excludePathPatterns("/");
		//swagger权限问题
		addInterceptorLogin.excludePathPatterns("/login/**","/swagger-resources/**");
		//swagger权限问题
		
		//addInterceptorLogin.excludePathPatterns("/");
		addInterceptorLogin.excludePathPatterns("/**/MP_verify_6bYstsCny1gVSKHO.txt");
		addInterceptorLogin.excludePathPatterns("swagger-ui.html");
		addInterceptorLogin.excludePathPatterns("*.html");
		addInterceptorLogin.excludePathPatterns("/**.html*");
		addInterceptorLogin.excludePathPatterns("/**/*.css*");
		addInterceptorLogin.excludePathPatterns("/**/*.js*");
		addInterceptorLogin.excludePathPatterns("/**/*.png*");
		addInterceptorLogin.excludePathPatterns("/**/*.jpg*");
		addInterceptorLogin.excludePathPatterns("/**/*.gif*");
		addInterceptorLogin.excludePathPatterns("/**/*.min.*");
		addInterceptorLogin.excludePathPatterns("/**/login");
		addInterceptorLogin.excludePathPatterns("/**/logout");
		addInterceptorLogin.excludePathPatterns("/webjars/**");
		addInterceptorLogin.excludePathPatterns("*.ico");
		addInterceptorLogin.excludePathPatterns("/**/*.html");
		super.addInterceptors(registry);
 
	}
 
	/**
	 * this is a method of the WebMvcConfigurerAdapter.class we can override the
	 * default value/achievements of spring boot and customize our own
	 * HttpMessageConverters.
	 * 
	 * @param converters
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		/*
		 * using the StringHttpMessageConverter to handle with simple String
		 * message.
		 */

		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setDefaultCharset(Charset.forName("UTF-8"));
		converters.add(stringConverter);
		/*
		 * using the FastJsonHttpMessageConverter to handle these below. 1.
		 * text/html;charset=UTF-8 a page(htm/html/jsp etc.) 2.
		 * application/json;charset=utf-8 json data type response 3.
		 * text/plain;charset=UTF-8 a text or string etc. 4.
		 * application/x-www-form-urlencoded;charset=utf-8 standard encoding
		 * type. convert form data to a key-value. ...
		 */
		FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setCharset(Charset.forName("UTF-8"));
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
		fastJsonConverter.setFastJsonConfig(fastJsonConfig);
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		MediaType text_plain = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
		MediaType text_html = new MediaType(MediaType.TEXT_HTML, Charset.forName("UTF-8"));
		MediaType x_www_form_urlencoded_utf8 = new MediaType(MediaType.APPLICATION_FORM_URLENCODED,
				Charset.forName("UTF-8"));
		supportedMediaTypes.add(text_html);
		supportedMediaTypes.add(text_plain);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		supportedMediaTypes.add(x_www_form_urlencoded_utf8);

		fastJsonConverter.setSupportedMediaTypes(supportedMediaTypes);
		converters.add(fastJsonConverter);
		super.configureMessageConverters(converters);

	}
	
	
	

}
