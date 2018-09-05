package com.weixin.note.serv.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;



/**
 * 用户表
 * 
 * @author 自动生成
 * @email 402376085@qq.com
 * @date 2018-08-27 17:05:38
 */
public class BillUser  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	/**主键**/
	@ApiModelProperty("主键")
	private Long id;
	
	/**用户名**/
	@ApiModelProperty("用户名")
	private String name;
	
	/**性别**/
	@ApiModelProperty("性别")
	private String sex;
	
	/**数据状态D已删除 E有效 A创建**/
	@ApiModelProperty("数据状态D已删除 E有效 A创建")
	private String dataFlag;
	
	/**创建时间**/
	@ApiModelProperty("创建时间")
	private Date createDatetime;
	
	/**修改时间**/
	@ApiModelProperty("修改时间")
	private Date updateDatetime;
	
	/**用户code码**/
	@ApiModelProperty("用户code码")
	private String code;

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
	 * 设置：用户名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：用户名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：性别
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * 获取：性别
	 */
	public String getSex() {
		return sex;
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
	/**
	 * 设置：用户code码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：用户code码
	 */
	public String getCode() {
		return code;
	}
}
