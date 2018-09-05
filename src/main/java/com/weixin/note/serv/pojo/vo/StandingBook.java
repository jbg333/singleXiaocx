package com.weixin.note.serv.pojo.vo;

import java.util.Date;

public class StandingBook {
	/**
	 * 数据id
	 */
	private Long id;
	/**
	 * 借钱时间
	 */
	private Date borrowMTime;
	/**
	 * 还钱时间
	 */
	private Date repayMTime;
	
	/**
	 * 实际还钱时间
	 */
	private Date realRepayTime;
	
	/**
	 * 借钱原因
	 */
	private String borrowReason;
	
	/**
	 * 还钱标志
	 */
	private String repayflag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBorrowMTime() {
		return borrowMTime;
	}

	public void setBorrowMTime(Date borrowMTime) {
		this.borrowMTime = borrowMTime;
	}

	public Date getRepayMTime() {
		return repayMTime;
	}

	public void setRepayMTime(Date repayMTime) {
		this.repayMTime = repayMTime;
	}

	public Date getRealRepayTime() {
		return realRepayTime;
	}

	public void setRealRepayTime(Date realRepayTime) {
		this.realRepayTime = realRepayTime;
	}

	public String getBorrowReason() {
		return borrowReason;
	}

	public void setBorrowReason(String borrowReason) {
		this.borrowReason = borrowReason;
	}

	public String getRepayflag() {
		return repayflag;
	}

	public void setRepayflag(String repayflag) {
		this.repayflag = repayflag;
	}
	
	
}
