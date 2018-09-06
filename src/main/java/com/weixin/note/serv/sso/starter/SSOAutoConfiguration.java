package com.weixin.note.serv.sso.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.weixin.note.serv.sso.config.SSOConfig;

/*@Configuration
@EnableConfigurationProperties(SSOProperties.class)
public class SSOAutoConfiguration {

    @Autowired
    private SSOProperties properties;

    *//**
     * 注入初始化
     *//*
    @Bean
    public SSOConfig getInstance() {
        return SSOConfig.init(properties.getConfig());
    }
}*/
