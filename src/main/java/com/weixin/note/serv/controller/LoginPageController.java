package com.weixin.note.serv.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weixin.note.serv.feign.ILoginPage;
import com.weixin.note.serv.pojo.enm.Action;
import com.weixin.note.serv.pojo.enm.Login;
import com.weixin.note.serv.pojo.entity.AccessTokenResponse;
import com.weixin.note.serv.pojo.vo.Person;
import com.weixin.note.serv.pojo.vo.StandingBook;
import com.weixin.note.serv.service.ILoginPageService;
import com.weixin.note.serv.util.Rt;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
@Api(tags = "登录相关接口")
@RestController
public class LoginPageController implements ILoginPage{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ILoginPageService iLoginPageService;
	
	@Login(Action.Skip)
	@ApiOperation(value="小程序用户登录", notes="提交")
	@ApiImplicitParam(name = "person", value = "用户实体", required = true, dataType = "Person", paramType = "body")
	@RequestMapping(value = { "/wx/login" }, method = {RequestMethod.POST})
	public Rt<String> login(@RequestParam HashMap<String,String> param,HttpServletRequest request) {
		String loginCode = param.get("code");
		String token = iLoginPageService.getUserLoginStat(loginCode, request);
		return Rt.ok(token);
	}
	
	
	
	
	
	@ApiOperation(value="用户登录", notes="提交")
	@ApiImplicitParam(name = "person", value = "用户实体", required = true, dataType = "Person", paramType = "body")
	public Rt<List<Person>> login(@RequestBody Person person) {
		return null;
	}

	@ApiOperation(value="用户登录", notes="提交")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "long", paramType = "path")
	public Rt<List<StandingBook>> getUserList(@PathVariable("userId") Long userId) {
		System.out.println("===========调用Service=========="+userId+"======");
		logger.info("测试日志{a}={b}", userId);
		return Rt.error("调用失败");
	}

	@Override
	public Rt<String> markRepay(@PathVariable("userId") Long userId, @PathVariable("rowId")Long rowId) {
		System.out.println("===========调用Service=========="+userId+"===="+rowId+"======");
		logger.info("测试日志{a}={b}", userId,rowId);
		return Rt.ok("操作成功");
	}

	@Override
	public Rt<String> saveBook(@RequestBody StandingBook book) {
		return null;
	}

	@Override
	public Rt<String> deltest(@PathVariable("userId") Long userId, Long id,Long id2) {
		logger.info("====={}============={}=========",userId,id,id2);
		return null;
	}

	
}
