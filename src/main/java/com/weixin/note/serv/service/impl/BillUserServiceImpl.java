package com.weixin.note.serv.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weixin.enm.DataStatusEnum;
import com.weixin.entity.BillUser;
import com.weixin.note.serv.dao.BillUserDao;
import com.weixin.note.serv.service.BillUserService;
import com.weixin.util.Query;




@Service("billUserService")
@Transactional
public class BillUserServiceImpl implements BillUserService {
	@Autowired
	private BillUserDao billUserDao;
	
	@Override
	public BillUser get(Long id){
		return billUserDao.get(id);
	}

	@Override
	public List<BillUser> getList(Map<String, Object> map){
		defaultDataFlag(map);
		return billUserDao.getList(map);
	}
	
	@Override
	public PageInfo<BillUser> queryPageList(Query query) {
		PageHelper.startPage(query.getPage(), query.getLimit());
		defaultDataFlag(query);
		List<BillUser> resultList = this.getList(query);
		PageInfo<BillUser> pageList = new PageInfo<BillUser>(resultList);
		return pageList;
	}


	/**
	 * 用于增加查询参数 如果传入参数不存在数据状态字段查询E状态的
	 * defaultDataFlag:(). 
	 * @author jbg  
	 * @param map  
	 * @since JDK 1.8
	 */
	private void defaultDataFlag(Map<String, Object> map) {
		if(!map.containsKey("dataFlag")) {//默认查询为E状态的数据
			map.put("dataFlag", DataStatusEnum.E.getCode());
		}
	}
	
	@Override
	public int getCount(Map<String, Object> map){
		defaultDataFlag(map);
		return billUserDao.getCount(map);
	}

	@Override
	public void save(BillUser billUser){
		if(StringUtils.isEmpty(billUser.getDataFlag())) {//默认插入 E状态数据
			billUser.setDataFlag(DataStatusEnum.E.getCode());
		}
		billUserDao.save(billUser);
	}

	@Override
	public void update(BillUser billUser){
		billUserDao.update(billUser);
	}

	@Override
	public void delete(Long id){
		billUserDao.delete(id);
	}

	@Override
	public void deleteBatch(Long[] ids){
		billUserDao.deleteBatch(ids);
	}
	
	 @Override
	public void logicDelete(Long id) {
    	BillUser billUser = new BillUser();
    	billUser.setId(id);
    	billUser.setDataFlag(DataStatusEnum.D.getCode());
    	billUserDao.update(billUser);
	}
	

    @Override
    public void updateDataFlag(Long[] ids,String stateValue) {
        for (Long id:ids){
			BillUser billUser=get(id);
			billUser.setDataFlag(stateValue);
            update(billUser);
        }
    }
	
}
