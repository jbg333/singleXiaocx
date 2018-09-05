package com.weixin.note.serv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableFeignClients(basePackages= {"com.jia.weixin.feign"})
//@EnableEurekaClient
@SpringBootApplication
@MapperScan(basePackages={"com.weixin.note.serv.dao"})
//@EnableSwagger2
public class WeixinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeixinServiceApplication.class, args);
	}
}
