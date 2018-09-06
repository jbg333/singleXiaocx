package com.weixin.note.serv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableFeignClients(basePackages= {"com.jia.weixin.feign"})
//@EnableEurekaClient
@SpringBootApplication
@MapperScan(basePackages={"com.weixin.note.serv.dao"})
@EnableSwagger2
public class WeixinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeixinServiceApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
