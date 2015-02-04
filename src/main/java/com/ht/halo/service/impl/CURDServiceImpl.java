package com.ht.halo.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ht.halo.base.Base;
import com.ht.halo.dao.IHaloDao;
import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.utils.StringUtils;
import com.ht.halo.service.ICURDService;

@Transactional
public abstract class CURDServiceImpl<T, PK extends Serializable> extends Base implements ICURDService<T, PK> {
	public abstract IHaloDao<T, PK> getDao();//
/*	 private void init(HaloMap parameter){
		  if(null!=parameter.get(HaloMap.ADDMETHOD)){
			  String method=(String) parameter.get(HaloMap.ADDMETHOD);
			  logger.info("将先执行方法:"+this.getClass().getSimpleName()+"中"+method);
			  MyBeanUtils.invoke(this, method, parameter, HaloMap.class);
			  parameter.remove(HaloMap.ADDMETHOD);
		  }
	   }*/
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

	public final int deleteById(PK id) {
		if(null==id){
			return 0;
		}
		if(id instanceof String){
			String idStr =String.valueOf(id);
			if(StringUtils.isBlank(idStr)){
				return 0;
			}
		}
		getDao().deleteById(id);
		return 1;
	}
	public  final int delete(T entity) {
		if(null==entity){
			return 0;
		}
		getDao().delete(entity);
		return 1;
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
		return getDao().findFirstByMap(parameter);
	}
 
	public final <X> List<X> findListByMap(HaloMap parameter) {
		if(null==parameter){
			parameter= new HaloMap();
		}
		return getDao().findListByMap(parameter);
	}
	public   final Page<T> findPageByMap(Page<T> page, HaloMap parameter) {
		if(null==parameter){
			parameter= new HaloMap();
		}
		return getDao().findPageByMap(page, parameter);
	}
	
}
