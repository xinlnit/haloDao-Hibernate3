package com.ht.halo.service.impl;

import java.io.Serializable;
import java.util.List;

import com.ht.halo.dao.IHaloDao;
import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.service.IReadService;

public abstract class ReadServiceImpl<T, PK extends Serializable> implements IReadService<T, PK> {
	public abstract IHaloDao<T, PK> getDao();// abstract

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
