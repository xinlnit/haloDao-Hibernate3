package com.ht.halo.service.impl;

import java.io.Serializable;

import com.ht.halo.dao.IHaloDao;
import com.ht.halo.service.ICRUDService;

public abstract class CRUDServiceImpl<T, PK extends Serializable> implements ICRUDService<T, PK> {
	public abstract IHaloDao<T, PK> getDao();// abstract


	public final T findById(PK id) {
		return getDao().queryById(id);
	}

	
	public T checkById(PK id) {
		return getDao().checkById(id);
	}

	public void add(T entity) {
		 getDao().save(entity);
	}

	public void modify(T entity) {
		 getDao().update(entity);
	}

	public void removeById(PK id) {
        getDao().deleteById(id);
	}

	public void remove(T entity) {
		getDao().delete(entity);
	}
}
