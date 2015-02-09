package com.ht.halo.hibernate3.utils.gson.bean;

import com.google.gson.GsonBuilder;

public class GsonHelpBean {
	private GsonBuilder gsonBuilder;
	private String[] fieldNames;

	public GsonBuilder getGsonBuilder() {
		return gsonBuilder;
	}

	public void setGsonBuilder(GsonBuilder gsonBuilder) {
		this.gsonBuilder = gsonBuilder;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	

}
