package com.weixin.note.serv.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weixin.note.serv.pojo.enm.DrcrEnum;
import com.weixin.note.serv.util.EnumUtil;

import io.swagger.annotations.ApiModelProperty;



/**
 * 账单明细表
 * 
 * @author 自动生成
 * @email 402376085@qq.com
 * @date 2018-08-27 17:05:38
 */
public class BillBookDetail  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	/**主键**/
	@ApiModelProperty("主键")
	private Long id;
	
	/**用户主键**/
	@ApiModelProperty("用户主键")
	private Long userId;
	
	@ApiModelProperty("用户微信主键")
	private String openId;
	
	/**1是借出 2是借入 **/
	@ApiModelProperty("1是借出 2是借入 ")
	private String drcr;
	
	
	/**对口人名**/
	@ApiModelProperty("对口人名")
	private String abutmentUserName;
	
	/**借出或者借入涉及金额（单位元）**/
	@ApiModelProperty("借出或者借入涉及金额（单位元）")
	private Double money;
	
	/**收借款或者还借款涉及金额（单位元）**/
	@ApiModelProperty("收借款或者还借款涉及金额（单位元）")
	private Double backMoney;
	
	/**备注信息**/
	@ApiModelProperty("备注信息")
	private String remark;
	
	/**数据状态D已删除 E有效 A创建**/
	@ApiModelProperty("数据状态D已删除 E有效 A创建")
	private String dataFlag;
	
	/**创建时间**/
	@ApiModelProperty("创建时间")
	private Date createDatetime;
	
	/**修改时间**/
	@ApiModelProperty("修改时间")
	private Date updateDatetime;
	
	/**事情发生日期**/
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty("事情发生日期")
	private Date drcrDate;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：用户主键
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户主键
	 */
	public Long getUserId() {
		return userId;
	}
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	/**
	 * 设置：1是借出 2是借入 
	 */
	public void setDrcr(String drcr) {
		this.drcr = drcr;
	}
	/**
	 * 获取：1是借出 2是借入 
	 */
	public String getDrcr() {
		return drcr;
	}
	
	public String getDrcrView() {
		if(drcr!=null) {
			String  drcrView=EnumUtil.getEnumByCode(DrcrEnum.class,drcr).getValue();
			return drcrView;
		}
		return null;
	}
	/**
	 * 设置：对口人名
	 */
	public void setAbutmentUserName(String abutmentUserName) {
		this.abutmentUserName = abutmentUserName;
	}
	/**
	 * 获取：对口人名
	 */
	public String getAbutmentUserName() {
		return abutmentUserName;
	}
	/**
	 * 设置：借出或者借入涉及金额（单位元）
	 */
	public void setMoney(Double money) {
		this.money = money;
	}
	/**
	 * 获取：借出或者借入涉及金额（单位元）
	 */
	public Double getMoney() {
		return money;
	}
	/**
	 * 设置：收借款或者还借款涉及金额（单位元）
	 */
	public void setBackMoney(Double backMoney) {
		this.backMoney = backMoney;
	}
	/**
	 * 获取：收借款或者还借款涉及金额（单位元）
	 */
	public Double getBackMoney() {
		return backMoney;
	}
	/**
	 * 设置：备注信息
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注信息
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：数据状态D已删除 E有效 A创建
	 */
	public void setDataFlag(String dataFlag) {
		this.dataFlag = dataFlag;
	}
	/**
	 * 获取：数据状态D已删除 E有效 A创建
	 */
	public String getDataFlag() {
		return dataFlag;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateDatetime() {
		return createDatetime;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateDatetime() {
		return updateDatetime;
	}
	public Date getDrcrDate() {
		return drcrDate;
	}
	public void setDrcrDate(Date drcrDate) {
		this.drcrDate = drcrDate;
	}
	
	
}
