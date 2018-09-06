package com.weixin.note.serv.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weixin.note.serv.pojo.entity.BillUser;
import com.weixin.note.serv.service.BillUserService;
import com.weixin.note.serv.sso.token.SSOToken;


@Service("ssoAuthorization")
public class SSOAuthorizationImpl implements SSOAuthorization{

	@Autowired
	BillUserService billUserService;
	
	@Override
	public boolean isPermitted(SSOToken token, String permission) {
		return true;
	}

	@Override
	public <T> T getObjectInfo( Object userId,Class<T> c) {
		//用户登录
		//SysUserService sysUserService = (SysUserService) SpringContextUtils.getBean("sysUserService");
		Long id = Long.valueOf(userId.toString());
		
		BillUser result = billUserService.get(id);
/*		R re = JSONObject.parseObject(result, Rt.class);
		if (re.getCode() == 200) {
			T tt = ParameterUtil.mapToBean(re.getData(), c);
			return tt;
		}
*/		return (T)result;
	}

}
