package com.weixin.note.serv.config;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.weixin.note.serv.pojo.enm.Action;
import com.weixin.note.serv.pojo.enm.Login;
import com.weixin.note.serv.sso.SSOHelper;
import com.weixin.note.serv.sso.config.SSOConfig;
import com.weixin.note.serv.sso.token.SSOToken;
import com.weixin.note.serv.sso.util.HttpUtil;
import com.weixin.note.serv.sso.util.SSOTokenContext;

public class SSOInterceptor extends HandlerInterceptorAdapter {
	protected final static Logger logger = LoggerFactory.getLogger(SSOInterceptor.class);
	private SSOHandlerInterceptor handlerInterceptor;


	/**
	 * 无注解情况下，设置为true，不进行权限验证
	 */
	private boolean nothingAnnotationPass = false;

	/**
	 * 登录权限验证
	 * <p>
	 * 方法拦截 Controller 处理之前进行调用。
	 * </p>
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		/**
		 * 处理 Controller 方法
		 * <p>
		 * 判断 handler 是否为 HandlerMethod 实例
		 * </p>
		 */
		if (handler instanceof HandlerMethod) {
			initHeaderContext(request);

			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Login login = method.getAnnotation(Login.class);
			if ((login != null) && (login.value() == Action.Skip)) {
				return true;
			}

			String overUrl = SSOConfig.getInstance().getOverUrl();
			boolean isOver = HttpUtil.inContainURL(request, overUrl);
			if (isOver) {
				return true;
			}
			/**
			 * 如果要是移动端，会从header和变量 "accessToken"中取得toke的值
			 */
			SSOToken ssoToken = SSOHelper.getSSOToken(request);
			if (ssoToken == null || ssoToken.getId() == null) {
				if (this.isNothingAnnotationPass()) {
					return true;
				}
				if (HttpUtil.isAjax(request) || SSOConfig.getInstance().isAjaxEnable()) {
					/*
					 * Handler 处理 AJAX 请求
					 */
					this.getHandlerInterceptor().preTokenIsNullAjax(request, response);
					return false;
				} else {
					/*
					 * token 为空，调用 Handler 处理 返回 true 继续执行，清理登录状态并重定向至登录界面
					 */
					if (this.getHandlerInterceptor().preTokenIsNull(request, response)) {
						logger.info("logout. request url:" + request.getRequestURL());
						SSOHelper.clearRedirectLogin(request, response);
					}
					return false;
				}
			} else {
				request.setAttribute(SSOConfig.getInstance().getAccessTokenName(), ssoToken);
				request.setAttribute(SSOConfig.getInstance().getAccessUserId(), ssoToken.getId());
				SSOTokenContext.getInstance().setSsoToken(ssoToken);
			}
		}
		return true;
	}
	
	public SSOHandlerInterceptor getHandlerInterceptor() {
		if (handlerInterceptor == null) {
			return KissoDefaultHandler.getInstance();
		}
		return handlerInterceptor;
	}

	public void setHandlerInterceptor(SSOHandlerInterceptor handlerInterceptor) {
		this.handlerInterceptor = handlerInterceptor;
	}


	public boolean isNothingAnnotationPass() {
		return nothingAnnotationPass;
	}

	public void setNothingAnnotationPass(boolean nothingAnnotationPass) {
		this.nothingAnnotationPass = nothingAnnotationPass;
	}

	/**
	 * 初始化，得到当前遥登录授权信息
	 * 
	 * @param request
	 */
	private void initHeaderContext(HttpServletRequest request) {
		Map<String, String> map = HttpUtil.getHeadersInfo(request);

		String token = map.get(SSOConfig.getInstance().getAccessTokenName());
		if (token == null || token.trim().equals("")) {
			token = request.getParameter(SSOConfig.getInstance().getAccessTokenName());
		}
		
		String userId = map.get(SSOConfig.getInstance().getAccessUserId());
		if (userId == null || userId.trim().equals("")) {
			userId = request.getParameter(SSOConfig.getInstance().getAccessUserId());
		}
		
		new SSOTokenContext.RequestHeaderContextBuild()
			.token(token)
			.userId(userId)
			.ssoToken(null).bulid();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		SSOTokenContext.clean();
		super.postHandle(request, response, handler, modelAndView);
	}

	 
}
