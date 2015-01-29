package com.ht.halo.service;

import java.io.Serializable;
import java.util.List;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;

public interface ICURDService <T, PK extends Serializable>{
	
	    public void save(T entity);
	    public void update(T entity);
	    public void updateNotNull(T entity);
	    public int updateNotNullByHql(T entity, HaloMap parameter);
	    public void deleteById(PK id);
	    public void delete(T entity);
		public int deleteByMap(HaloMap parameter);
	    public T findById(PK id);
	    public T checkById(PK id);
	    public T findFirstByMap(HaloMap parameter);
	    public <X> List<X> findListByMap(HaloMap parameter);
	    public Page<T> findPageByMap(Page<T> page, HaloMap parameter);   
}
