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
import com.weixin.entity.BillPhoneBook;
import com.weixin.note.serv.dao.BillPhoneBookDao;
import com.weixin.note.serv.service.BillPhoneBookService;
import com.weixin.util.Query;




@Service("billPhoneBookService")
@Transactional
public class BillPhoneBookServiceImpl implements BillPhoneBookService {
	@Autowired
	private BillPhoneBookDao billPhoneBookDao;
	
	@Override
	public BillPhoneBook get(Long id){
		return billPhoneBookDao.get(id);
	}

	@Override
	public List<BillPhoneBook> getList(Map<String, Object> map){
		defaultDataFlag(map);
		return billPhoneBookDao.getList(map);
	}
	
	@Override
	public PageInfo<BillPhoneBook> queryPageList(Query query) {
		PageHelper.startPage(query.getPage(), query.getLimit());
		defaultDataFlag(query);
		List<BillPhoneBook> resultList = this.getList(query);
		PageInfo<BillPhoneBook> pageList = new PageInfo<BillPhoneBook>(resultList);
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
		return billPhoneBookDao.getCount(map);
	}

	@Override
	public void save(BillPhoneBook billPhoneBook){
		if(StringUtils.isEmpty(billPhoneBook.getDataFlag())) {//默认插入 E状态数据
			billPhoneBook.setDataFlag(DataStatusEnum.E.getCode());
		}
		billPhoneBookDao.save(billPhoneBook);
	}

	@Override
	public void update(BillPhoneBook billPhoneBook){
		billPhoneBookDao.update(billPhoneBook);
	}

	@Override
	public void delete(Long id){
		billPhoneBookDao.delete(id);
	}

	@Override
	public void deleteBatch(Long[] ids){
		billPhoneBookDao.deleteBatch(ids);
	}
	
	 @Override
	public void logicDelete(Long id) {
    	BillPhoneBook billPhoneBook = new BillPhoneBook();
    	billPhoneBook.setId(id);
    	billPhoneBook.setDataFlag(DataStatusEnum.D.getCode());
    	billPhoneBookDao.update(billPhoneBook);
	}
	

    @Override
    public void updateDataFlag(Long[] ids,String stateValue) {
        for (Long id:ids){
			BillPhoneBook billPhoneBook=get(id);
			billPhoneBook.setDataFlag(stateValue);
            update(billPhoneBook);
        }
    }
	
}
