package com.weixin.note.serv.feign;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weixin.note.serv.pojo.entity.BillPhoneBook;
import com.weixin.note.serv.util.Rt;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



/**
 * 电话本
 * 
 * @author 自动生成
 * @email 402376085@qq.com
 * @date 2018-09-04 10:14:15
 */
@Api(tags = "BillPhoneBookController")
@RequestMapping("billphonebook")
public interface IBillPhoneBookController {
    /**
     * 跳转到列表页
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public String list();
    
	/**
	 * 列表数据
	 */
	@ApiOperation("列表查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "params", value = "查询参数", required = true, dataType = "Map", paramType = "query")
	})
	@ApiResponses(value = { 
			@ApiResponse(response=BillPhoneBook.class,code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
	@RequestMapping(value = "/listData", method = {RequestMethod.POST})
	public Rt<BillPhoneBook> listData(@RequestParam Map<String, Object> params);
	
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
			@ApiResponse(response=BillPhoneBook.class,responseContainer="List",code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
  	@RequestMapping(value = "/listDataNoPage", method = {RequestMethod.POST})
  	public Rt<List<BillPhoneBook>> listDataNoPage(@RequestParam Map<String, Object> params);

    /**
     * 跳转到新增页面
     **/
    @RequestMapping(value = "/add", method = {RequestMethod.GET})
    public String add();

    /**
     *   跳转到修改页面
     **/
    @RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
    public String edit(Model model, @PathVariable("id") Long id);

	/**
	 * 信息
	 */
	@ApiOperation("根据id查询数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "数据id", required = true, dataType = "Long", paramType = "path")
	})
	@ApiResponses(value = { 
			@ApiResponse(response=BillPhoneBook.class,code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
    @RequestMapping(value = "/info/{id}", method = {RequestMethod.GET})
    public Rt<BillPhoneBook> info(@PathVariable("id") Long id);

    /**
	 * 保存
	 */
	@ApiOperation("保存数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "billPhoneBook", value = "数据实体", required = true, dataType = "BillPhoneBook", paramType = "query")
	})
	@ApiResponses(value = { 
			@ApiResponse(response=String.class,code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public Rt<String> save(@RequestBody BillPhoneBook billPhoneBook);
	/**
	 * 修改
	 */
	@ApiOperation("修改数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "billPhoneBook", value = "数据实体", required = true, dataType = "BillPhoneBook", paramType = "query")
	})
	@ApiResponses(value = { 
			@ApiResponse(response=String.class,code = 200, message = "操作成功"),
			@ApiResponse(code = 500, message = "内部错误，信息由msg字段返回")
	})	
    @ResponseBody
	@RequestMapping(value = "/update", method = {RequestMethod.POST})
	public Rt<String> update(@RequestBody BillPhoneBook billPhoneBook);

    /**
     * 启用
     */
    @ResponseBody
    @RequestMapping(value = "/enable", method = {RequestMethod.POST})
    public Rt<String> enable(@RequestBody Long[] ids);
    /**
     * 禁用
     */
    @ResponseBody
    @RequestMapping(value = "/limit", method = {RequestMethod.POST})
    public Rt<String> limit(@RequestBody Long[] ids);
	
	/**
	 * 删除
	 */
    @ResponseBody
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public Rt<String> delete(@RequestBody Long[] ids);
	
	
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
	@RequestMapping(value = "/logicDelete/{id}", method = {RequestMethod.POST})
	public Rt<String> logicDelete(@PathVariable("id") Long id);
	
}
