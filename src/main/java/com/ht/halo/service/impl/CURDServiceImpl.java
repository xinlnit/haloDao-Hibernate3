package com.ht.halo.service.impl;

import java.io.Serializable;
import java.util.List;

import com.ht.halo.dao.IHaloDao;
import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.service.ICURDService;

public abstract class CURDServiceImpl<T, PK extends Serializable> implements ICURDService<T, PK> {
	public abstract IHaloDao<T, PK> getDao();// abstract

	

	public void save(T entity) {
		getDao().save(entity);
	}

	public void update(T entity) {
		getDao().update(entity);
	}

	public void updateNotNull(T entity) {
		getDao().updateWithNotNull(entity);
	}

	public int updateNotNullByHql(T entity, HaloMap parameter) {
		return getDao().updateWithNotNullByHql(entity);
	}

	public void deleteById(PK id) {
		getDao().deleteById(id);
	}

	public void delete(T entity) {
		getDao().delete(entity);
	}

	public int deleteByMap(HaloMap parameter) {
		return getDao().deleteByMap(parameter);
	}
	public final T findById(PK id) {
		return getDao().findById(id);
	}

	public T checkById(PK id) {
		return getDao().checkById(id);
	}
	public T findFirstByMap(HaloMap parameter) {
		return getDao().findFirstByMap(parameter);
	}

	public <X> List<X> findListByMap(HaloMap parameter) {
		return getDao().findListByMap(parameter);
	}

	public Page<T> findPageByMap(Page<T> page, HaloMap parameter) {
		return getDao().findPageByMap(page, parameter);
	}
}
