package com.weixin.note.serv.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weixin.note.serv.dao.BillBookDetailDao;
import com.weixin.note.serv.pojo.enm.DataStatusEnum;
import com.weixin.note.serv.pojo.entity.BillBookDetail;
import com.weixin.note.serv.service.BillBookDetailService;
import com.weixin.note.serv.util.Query;




@Service("billBookDetailService")
@Transactional
public class BillBookDetailServiceImpl implements BillBookDetailService {
	@Autowired
	private BillBookDetailDao billBookDetailDao;
	
	@Override
	public BillBookDetail get(Long id){
		return billBookDetailDao.get(id);
	}

	@Override
	public List<BillBookDetail> getList(Map<String, Object> map){
		defaultDataFlag(map);
		return billBookDetailDao.getList(map);
	}
	
	@Override
	public PageInfo<BillBookDetail> queryPageList(Query query) {
		PageHelper.startPage(query.getPage(), query.getLimit());
		defaultDataFlag(query);
		List<BillBookDetail> resultList = this.getList(query);
		PageInfo<BillBookDetail> pageList = new PageInfo<BillBookDetail>(resultList);
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
		return billBookDetailDao.getCount(map);
	}

	@Override
	public void save(BillBookDetail billBookDetail){
		if(StringUtils.isEmpty(billBookDetail.getDataFlag())) {//默认插入 E状态数据
			billBookDetail.setDataFlag(DataStatusEnum.E.getCode());
		}
		billBookDetailDao.save(billBookDetail);
	}

	@Override
	public void update(BillBookDetail billBookDetail){
		billBookDetailDao.update(billBookDetail);
	}

	@Override
	public void delete(Long id){
		billBookDetailDao.delete(id);
	}

	@Override
	public void deleteBatch(Long[] ids){
		billBookDetailDao.deleteBatch(ids);
	}
	
	 @Override
	public void logicDelete(Long id) {
    	BillBookDetail billBookDetail = new BillBookDetail();
    	billBookDetail.setId(id);
    	billBookDetail.setDataFlag(DataStatusEnum.D.getCode());
    	billBookDetailDao.update(billBookDetail);
	}
	

    @Override
    public void updateDataFlag(Long[] ids,String stateValue) {
        for (Long id:ids){
			BillBookDetail billBookDetail=get(id);
			billBookDetail.setDataFlag(stateValue);
            update(billBookDetail);
        }
    }
	
}
