package com.ht.halo.service;

import java.io.Serializable;
import java.util.List;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;

public interface IReadService<T, PK extends Serializable> {
	public T findById(PK id);

	public T checkById(PK id);

	public T findFirstByMap(HaloMap parameter);

	public <X> List<X> findListByMap(HaloMap parameter);

	public Page<T> findPageByMap(Page<T> page, HaloMap parameter);
}
