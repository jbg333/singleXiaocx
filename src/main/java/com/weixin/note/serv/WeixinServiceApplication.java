package com.weixin.note.serv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.weixin.note.serv.config.WxMappingJackson2HttpMessageConverter;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableFeignClients(basePackages= {"com.jia.weixin.feign"})
//@EnableEurekaClient
@SpringBootApplication
@MapperScan(basePackages={"com.weixin.note.serv.dao"})
public class WeixinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeixinServiceApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
		return restTemplate;
	}
}
