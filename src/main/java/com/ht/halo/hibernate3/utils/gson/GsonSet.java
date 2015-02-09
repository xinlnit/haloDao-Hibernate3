package com.ht.halo.hibernate3.utils.gson;

public class GsonSet {
	private String dateFormat;
	private Object hibernateFlag ;

	public String getDateFormat() {
		return dateFormat;
	}

	public GsonSet setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	public Object getHibernateFlag() {
		return hibernateFlag;
	}

	public GsonSet setHibernateFlag(Object hibernateFlag) {
		this.hibernateFlag = hibernateFlag;
		return this;
	}





}
