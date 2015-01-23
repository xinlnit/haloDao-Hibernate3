package com.ht.halo.service.impl;

import java.io.Serializable;

import com.ht.halo.hibernate3.HaloDao;
import com.ht.halo.service.ICRUDService;


public abstract class CRUDServiceImpl<T, PK extends Serializable> implements ICRUDService<T, PK>{
	public abstract HaloDao<T,PK> getDao();
	@Override
	public T findById(PK id) {
		return getDao().get(id);
	}
	
}

