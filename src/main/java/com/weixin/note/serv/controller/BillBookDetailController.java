package com.weixin.note.serv.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.weixin.note.serv.feign.IBillBookDetail;
import com.weixin.note.serv.pojo.entity.BillBookDetail;
import com.weixin.note.serv.service.BillBookDetailService;
import com.weixin.note.serv.sso.SSOHelper;
import com.weixin.note.serv.sso.token.SSOToken;
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
 * 账单明细表
 * 
 * @author 自动生成
 * @email 402376085@qq.com
 * @date 2018-08-27 17:05:38
 */
@Api(tags = "账单详情")
@Controller
@RequestMapping("billbookdetail")
public class BillBookDetailController{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BillBookDetailService billBookDetailService;
	
    /**
     * 跳转到列表页
     */
    public String list() {
        return "billbookdetail/list";
    }
    
	/**
	 * 列表数据
	 */
	@ApiOperation("列表查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "params", value = "查询参数", required = true, dataType = "Map", paramType = "query")
	})
	@ApiResponses(value = { 
			@ApiResponse(response=BillBookDetail.class,code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
	@RequestMapping(value = "/listData", method = {RequestMethod.POST})
	public Rt<BillBookDetail> listData(@RequestParam Map<String, Object> params,HttpServletRequest request){
		//查询列表数据
		try {
			SSOToken token =  SSOHelper.getSSOToken(request);
			params.put("openId", token.getId());
			Query query = new Query(params);
	    	PageInfo<BillBookDetail> billBookDetailResult = billBookDetailService.queryPageList(query);
	    	RtPageUtils<BillBookDetail> pageUtil = new RtPageUtils<>(billBookDetailResult.getList(), billBookDetailResult.getTotal(), query.getLimit(), query.getPage());
	    	return Rt.ok(pageUtil);
	   } catch (Exception e) {
			logger.error("查询列表失败",e);
			return Rt.error("查询列表失败");
		}
	}
	
	
	 /**
     * 
     * listDataNoPage:(不分页查询). 
     * @author jbg  
     * @param params
     * @return  
     * @since JDK 1.8
     */
    @ApiOperation("列表查询不分页")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "params", value = "查询参数", required = true, dataType = "Map", paramType = "query")
	})
	@ApiResponses(value = { 
			@ApiResponse(response=BillBookDetail.class,responseContainer="List",code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
  	@RequestMapping(value = "/listDataNoPage", method = {RequestMethod.POST})
  	public Rt<List<BillBookDetail>> listDataNoPage(@RequestParam Map<String, Object> params){
  		//查询列表数据
    	List<BillBookDetail> list = billBookDetailService.getList(params);
  		return Rt.ok(list);
  	}

    /**
     * 跳转到新增页面
     **/
    @RequestMapping(value = "/add", method = {RequestMethod.GET})
    public String add(){
        return "billbookdetail/add";
    }

    /**
     *   跳转到修改页面
     **/
    @RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
    public String edit(Model model, @PathVariable("id") Long id){
		BillBookDetail billBookDetail = billBookDetailService.get(id);
        model.addAttribute("model",billBookDetail);
        return "billbookdetail/edit";
    }

	/**
	 * 信息
	 */
	@ApiOperation("根据id查询数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "数据id", required = true, dataType = "Long", paramType = "path")
	})
	@ApiResponses(value = { 
			@ApiResponse(response=BillBookDetail.class,code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
    @RequestMapping(value = "/info/{id}", method = {RequestMethod.GET})
    public Rt<BillBookDetail> info(@PathVariable("id") Long id){
	    try {
	       
	        BillBookDetail billBookDetail = billBookDetailService.get(id);
	        return Rt.ok(billBookDetail);
		} catch (Exception e) {
				logger.error("查询失败",e);
				return Rt.error("查询失败");
		}
    
    }

    /**
	 * 保存
	 */
	@ApiOperation("保存数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "billBookDetail", value = "数据实体", required = true, dataType = "BillBookDetail", paramType = "query")
	})
	@ApiResponses(value = { 
			@ApiResponse(response=String.class,code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public Rt<String> save(@RequestBody BillBookDetail billBookDetail,HttpServletRequest request){
		 try {
			 SSOToken token =  SSOHelper.getSSOToken(request);
			 billBookDetail.setOpenId(token.getId());
			billBookDetailService.save(billBookDetail);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("保存失败",e);
			return Rt.error("保存失败");
		}
	}
	
	/**
	 * 修改
	 */
	@ApiOperation("修改数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "billBookDetail", value = "数据实体", required = true, dataType = "BillBookDetail", paramType = "query")
	})
	@ApiResponses(value = { 
			@ApiResponse(response=String.class,code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
	@RequestMapping(value = "/update", method = {RequestMethod.POST})
	public Rt<String> update(@RequestBody BillBookDetail billBookDetail){
		 try {
			billBookDetailService.update(billBookDetail);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("修改失败",e);
			return Rt.error("修改失败");
		}
	}

    /**
     * 启用
     */
    @ResponseBody
    @RequestMapping(value = "/enable", method = {RequestMethod.POST})
    public Rt<String> enable(@RequestBody Long[] ids){
        /*String stateValue=StateEnum.ENABLE.getCode();
		billBookDetailService.updateDataFlag(ids,stateValue);*/
        return Rt.ok();
    }
    /**
     * 禁用
     */
    @ResponseBody
    @RequestMapping(value = "/limit", method = {RequestMethod.POST})
    public Rt<String> limit(@RequestBody Long[] ids){
       /* String stateValue=StateEnum.LIMIT.getCode();
		billBookDetailService.updateDataFlag(ids,stateValue);*/
        return Rt.ok();
    }
	
	/**
	 * 删除
	 */
    @ResponseBody
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	
	public Rt<String> delete(@RequestBody Long[] ids){
		 try {
			billBookDetailService.deleteBatch(ids);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("删除失败",e);
			return Rt.error("删除失败");
		}
	}
	
	
	/**
	 * 逻辑删除
	 */
	@ApiOperation("逻辑删除数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Long", value = "数据实体", required = true, dataType = "Long", paramType = "path")
	})
	@ApiResponses(value = { 
			@ApiResponse(response=String.class,code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
	@RequestMapping(value = "/logicDelete/{id}", method = {RequestMethod.GET})
	public Rt<String> logicDelete(@PathVariable("id") Long id){
		 try {
			billBookDetailService.logicDelete(id);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("删除失败",e);
			return Rt.error("删除失败");
		}
	}
	
}
