package com.ht.test.entity;

import java.math.BigDecimal;

import com.ht.halo.hibernate3.utils.annotations.HaloView;

@HaloView(position="test")
public class ViewHalo {
	private String receivableDetailId;
	private String houseName;
	private Boolean flag;
	private BigDecimal money;

	//@Id
	public String getReceivableDetailId() {
		return receivableDetailId;
	}
	public void setReceivableDetailId(String receivableDetailId) {
		this.receivableDetailId = receivableDetailId;
	}
	public String getHouseName() {
		return houseName;
	}
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	

}
