package com.weixin.note.serv.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.jia.weixin.feign.bill.IBillPhoneBookController;
import com.weixin.entity.BillPhoneBook;
import com.weixin.note.serv.service.BillPhoneBookService;
import com.weixin.util.Query;
import com.weixin.util.Rt;
import com.weixin.util.RtPageUtils;

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
public class BillPhoneBookController implements IBillPhoneBookController{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BillPhoneBookService billPhoneBookService;
	
    public String list() {
        return "billphonebook/list";
    }
    
	public Rt<BillPhoneBook> listData(@RequestParam Map<String, Object> params){
		//查询列表数据
		try {
			Query query = new Query(params);
	    	PageInfo<BillPhoneBook> billPhoneBookResult = billPhoneBookService.queryPageList(query);
	    	RtPageUtils<BillPhoneBook> pageUtil = new RtPageUtils<>(billPhoneBookResult.getList(), billPhoneBookResult.getTotal(), query.getLimit(), query.getPage());
	    	return Rt.ok(pageUtil);
	   } catch (Exception e) {
			logger.error("查询列表失败",e);
			return Rt.error("查询列表失败");
		}
	}
	
  	public Rt<List<BillPhoneBook>> listDataNoPage(@RequestParam Map<String, Object> params){
  		//查询列表数据
    	List<BillPhoneBook> list = billPhoneBookService.getList(params);
  		return Rt.ok(list);
  	}

    public String add(){
        return "billphonebook/add";
    }

    public String edit(Model model, @PathVariable("id") Long id){
		BillPhoneBook billPhoneBook = billPhoneBookService.get(id);
        model.addAttribute("model",billPhoneBook);
        return "billphonebook/edit";
    }

    public Rt<BillPhoneBook> info(@PathVariable("id") Long id){
	    try {
	       
	        BillPhoneBook billPhoneBook = billPhoneBookService.get(id);
	        return Rt.ok(billPhoneBook);
		} catch (Exception e) {
				logger.error("查询失败",e);
				return Rt.error("查询失败");
		}
    
    }

	public Rt<String> save(@RequestBody BillPhoneBook billPhoneBook){
		 try {
			billPhoneBookService.save(billPhoneBook);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("保存失败",e);
			return Rt.error("保存失败");
		}
	}
	
	public Rt<String> update(@RequestBody BillPhoneBook billPhoneBook){
		 try {
			billPhoneBookService.update(billPhoneBook);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("修改失败",e);
			return Rt.error("修改失败");
		}
	}

    public Rt<String> enable(@RequestBody Long[] ids){
  /*      String stateValue=StateEnum.ENABLE.getCode();
		billPhoneBookService.updateDataFlag(ids,stateValue);*/
        return Rt.ok();
    }
    public Rt<String> limit(@RequestBody Long[] ids){
        /*String stateValue=StateEnum.LIMIT.getCode();
		billPhoneBookService.updateDataFlag(ids,stateValue);*/
        return Rt.ok();
    }
	public Rt<String> delete(@RequestBody Long[] ids){
		 try {
			billPhoneBookService.deleteBatch(ids);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("删除失败",e);
			return Rt.error("删除失败");
		}
	}
	
	public Rt<String> logicDelete(@PathVariable("id") Long id){
		 try {
			billPhoneBookService.logicDelete(id);
			return Rt.ok();
		} catch (Exception e) {
			logger.error("删除失败",e);
			return Rt.error("删除失败");
		}
	}
	
}
