package com.weixin.note.serv.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.weixin.entity.BillPhoneBook;
import com.weixin.util.Query;

/**
 * 电话本
 * 
 * @author 自动生成
 * @email 402376085@qq.com
 * @date 2018-09-04 10:14:15
 */
public interface BillPhoneBookService {
	/**
	 * 根据主键查询信息
	 */
	BillPhoneBook get(Long id);
	/**
	*不分页数据
	*/
	List<BillPhoneBook> getList(Map<String, Object> map);
	/**
	 * 列表数据
	 */
	PageInfo<BillPhoneBook> queryPageList(Query query);
	/**
	*统计数量
	*/
	int getCount(Map<String, Object> map);
	/**
	*保存
	*/
	void save(BillPhoneBook billPhoneBook);
	/**
	*修改
	*/
	void update(BillPhoneBook billPhoneBook);
	/**
	*删除
	*/
	void delete(Long id);
	/**
	*批量删除
	*/
	void deleteBatch(Long[] ids);
	
	/**
	 * 逻辑删除
	 * logicDelete:(). 
	 * @author jbg  
	 * @param id  
	 * @since JDK 1.8
	 */
	void logicDelete(Long id);
	/**
	*修改状态
	*/
    void updateDataFlag(Long[] ids, String stateValue);
}
