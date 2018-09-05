package com.weixin.note.serv.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;



/**
 * 电话本
 * 
 * @author 自动生成
 * @email 402376085@qq.com
 * @date 2018-09-04 10:14:15
 */
public class BillPhoneBook implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	/**主键**/
	@ApiModelProperty("主键")
	private Long id;
	
	/**用户主键**/
	@ApiModelProperty("用户主键")
	private Long userId;
	
	/**电话簿记录名**/
	@ApiModelProperty("电话簿记录名")
	private String bookUserName;
	
	/**电话号码**/
	@ApiModelProperty("电话号码")
	private String bookUserPhone;
	
	/**数据状态D已删除 E有效 A创建**/
	@ApiModelProperty("数据状态D已删除 E有效 A创建")
	private String dataFlag;
	
	/**创建时间**/
	@ApiModelProperty("创建时间")
	private Date createDatetime;
	
	/**修改时间**/
	@ApiModelProperty("修改时间")
	private Date updateDatetime;

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
	/**
	 * 设置：电话簿记录名
	 */
	public void setBookUserName(String bookUserName) {
		this.bookUserName = bookUserName;
	}
	/**
	 * 获取：电话簿记录名
	 */
	public String getBookUserName() {
		return bookUserName;
	}
	/**
	 * 设置：电话号码
	 */
	public void setBookUserPhone(String bookUserPhone) {
		this.bookUserPhone = bookUserPhone;
	}
	/**
	 * 获取：电话号码
	 */
	public String getBookUserPhone() {
		return bookUserPhone;
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
}
