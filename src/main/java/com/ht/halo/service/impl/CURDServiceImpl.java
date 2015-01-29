package com.ht.halo.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ht.halo.dao.IHaloDao;
import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.MyBeanUtils;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.service.ICURDService;

@Transactional
public abstract class CURDServiceImpl<T, PK extends Serializable> implements ICURDService<T, PK> {
	public abstract IHaloDao<T, PK> getDao();// abstract
	 private void init(HaloMap parameter){
		  if(null!=parameter.get(HaloMap.ADDMETHOD)){
			  String method=(String) parameter.get(HaloMap.ADDMETHOD);
			  MyBeanUtils.invoke(this, method, parameter, HaloMap.class);
			  parameter.remove(HaloMap.ADDMETHOD);
		  }
	   }
	public void initSave(T entity) {
	}

	public  final void save(T entity) {
		initSave(entity);
		getDao().save(entity);
	}

	public  void initUpdate(T entity) {
	}
	public final void update(T entity) {
		initUpdate(entity);
		getDao().update(entity);
	}

	public  void initUpdateNotNull(T entity) {
	}
	public final void updateNotNull(T entity) {
		initUpdate(entity);
		getDao().updateWithNotNull(entity);
	}

	public final int updateNotNullByHql(T entity, HaloMap parameter) {
		return getDao().updateWithNotNullByHql(entity);
	}

	public final void deleteById(PK id) {
		getDao().deleteById(id);
	}
	public  final void delete(T entity) {
		getDao().delete(entity);
	}

	public final int deleteByMap(HaloMap parameter) {
		return getDao().deleteByMap(parameter);
	}

	public final T findById(PK id) {
		return getDao().findById(id);
	}

	public final T checkById(PK id) {
		return getDao().checkById(id);
	}
	public final T  findFirstByMap(HaloMap parameter) {
		init(parameter);
		return getDao().findFirstByMap(parameter);
	}
 
	public final <X> List<X> findListByMap(HaloMap parameter) {
		if(null==parameter){
			parameter= new HaloMap();
		}
		init(parameter);
		return getDao().findListByMap(parameter);
	}
	public   final Page<T> findPageByMap(Page<T> page, HaloMap parameter) {
		if(null==parameter){
			parameter= new HaloMap();
		}
		init(parameter);
		return getDao().findPageByMap(page, parameter);
	}
	
}
