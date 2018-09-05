package com.weixin.note.serv.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jia.weixin.feign.loginpage.ILoginPage;
import com.weixin.util.Rt;
import com.weixin.vo.Person;
import com.weixin.vo.StandingBook;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
@RestController
public class LoginPageController implements ILoginPage{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
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
