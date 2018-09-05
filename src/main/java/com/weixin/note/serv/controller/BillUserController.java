package com.weixin.note.serv.controller;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.weixin.note.serv.feign.IBillUser;
import com.weixin.note.serv.pojo.entity.BillUser;
import com.weixin.note.serv.service.BillUserService;
import com.weixin.note.serv.util.Query;
import com.weixin.note.serv.util.Rt;
import com.weixin.note.serv.util.RtPageUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 用户表
 * 
 * @author 自动生成
 * @email 402376085@qq.com
 * @date 2018-08-27 17:05:38
 */
@Api(tags = "BillUserController")
@Controller
public class BillUserController implements IBillUser {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BillUserService billUserService;
	/**
	 * 跳转到列表页
	 */
	public String list() {
		return "billuser/list";
	}
	@ResponseBody
	public Rt<BillUser> listData(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		try {
			Query query = new Query(params);
			PageInfo<BillUser> billUserResult = billUserService.queryPageList(query);
			RtPageUtils<BillUser> pageUtil = new RtPageUtils<>(billUserResult.getList(), billUserResult.getTotal(),
					query.getLimit(), query.getPage());
			return Rt.ok(pageUtil);
		} catch (Exception e) {
			logger.error("查询列表失败", e);
			return Rt.error("查询列表失败");
		}
	}
	/**
	 * 
	 * listDataNoPage:(不分页查询).
	 * 
	 * @author jbg
	 * @param params
	 * @return
	 * @since JDK 1.8
	 */
	@ApiOperation("列表查询不分页")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "params", value = "查询参数", required = true, dataType = "Map", paramType = "query") })
	@ApiResponses(value = {
			@ApiResponse(response = BillUser.class, responseContainer = "List", code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回") })
	@ResponseBody
	public Rt<List<BillUser>> listDataNoPage(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		List<BillUser> list = billUserService.getList(params);
		return Rt.ok(list);
	}
	/**
	 * 跳转到新增页面
	 **/
	public String add() {
		return "billuser/add";
	}
	/**
	 * 跳转到修改页面
	 **/
	public String edit(Model model, @PathVariable("id") Long id) {
		BillUser billUser = billUserService.get(id);
		model.addAttribute("model", billUser);
		return "billuser/edit";
	}
	/**
	 * 信息
	 */
	@ResponseBody
	public Rt<BillUser> info(@PathVariable("id") Long id) {
		try {
			BillUser billUser = billUserService.get(id);
			return Rt.ok(billUser);
		} catch (Exception e) {
			logger.error("查询失败", e);
			return Rt.error("查询失败");
		}
	}
	/**
	 * 保存
	 */
	@ResponseBody
	public Rt<String> save(@RequestBody BillUser billUser) {
		try {
			billUserService.save(billUser);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("保存失败", e);
			return Rt.error("保存失败");
		}
	}
	/**
	 * 修改
	 */
	@ResponseBody
	public Rt<String> update(@RequestBody BillUser billUser) {
		try {
			billUserService.update(billUser);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("修改失败", e);
			return Rt.error("修改失败");
		}
	}
	/**
	 * 启用
	 */
	@ResponseBody
	public Rt<String> enable(@RequestBody Long[] ids) {
		/*
		 * String stateValue=StateEnum.ENABLE.getCode();
		 * billUserService.updateDataFlag(ids,stateValue);
		 */
		return Rt.ok();
	}
	/**
	 * 禁用
	 */
	@ResponseBody
	public Rt<String> limit(@RequestBody Long[] ids) {
		/*
		 * String stateValue=StateEnum.LIMIT.getCode();
		 * billUserService.updateDataFlag(ids,stateValue);
		 */
		return Rt.ok();
	}
	/**
	 * 删除
	 */
	@ResponseBody
	public Rt<String> delete(@RequestBody Long[] ids) {
		try {
			billUserService.deleteBatch(ids);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("删除失败", e);
			return Rt.error("删除失败");
		}
	}

	@ResponseBody
	public Rt<String> logicDelete(@PathVariable("id") Long id) {
		try {
			billUserService.logicDelete(id);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("删除失败", e);
			return Rt.error("删除失败");
		}
	}
}
