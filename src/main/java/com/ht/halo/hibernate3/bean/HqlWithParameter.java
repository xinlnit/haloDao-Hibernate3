package com.ht.halo.hibernate3.bean;

import com.ht.halo.hibernate3.HaloMap;

public class HqlWithParameter {
	private String hql;
	private HaloMap paramterMap;
	private Boolean addColumn=false;
	private Boolean addCache=false;

	public Boolean getAddColumn() {
		return addColumn;
	}

	public void setAddColumn(Boolean addColumn) {
		this.addColumn = addColumn;
	}

	public Boolean getAddCache() {
		return addCache;
	}

	public void setAddCache(Boolean addCache) {
		this.addCache = addCache;
	}

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public HaloMap getParamterMap() {
		return paramterMap;
	}

	public void setParamterMap(HaloMap paramterMap) {
		this.paramterMap = paramterMap;
	}





}
