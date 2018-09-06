
package com.weixin.note.serv.sso.service;

import com.weixin.note.serv.sso.config.SSOConfig;

/**
 * <p>
 * SSO 单点登录服务抽象实现类s
 * </p>
 *
 * @author hubin
 * @since 2015-12-03
 */
public class ConfigurableAbstractKissoService extends AbstractKissoService {

    public ConfigurableAbstractKissoService() {
        config = SSOConfig.getInstance();
    }






 
	
}
