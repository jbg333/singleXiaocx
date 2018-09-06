package com.weixin.note.serv.sso.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.weixin.note.serv.sso.config.SSOConfig;

/**
 * <p>
 * Spring boot starter 启动配置
 * </p>
 *
 * @author hubin
 * @since 2017-07-18
 */
@Component
@PropertySource(value = "classpath:auth/sso.properties")
@ConfigurationProperties(prefix = "sso")
public class SSOProperties {


    private SSOConfig config;
    
    

    public SSOConfig getConfig() {
        return config;
    }

    public void setConfig(SSOConfig config) {
        this.config = config;
    }

	
    
    
}
