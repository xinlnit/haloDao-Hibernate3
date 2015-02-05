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

	/**
	 * TODO 创建对象
	 * 
	 * @return
	 */
	public abstract T create();
	public abstract PK getId(T entity);

	/*
	 * private void init(HaloMap parameter){
	 * if(null!=parameter.get(HaloMap.ADDMETHOD)){ String method=(String)
	 * parameter.get(HaloMap.ADDMETHOD);
	 * logger.info("将先执行方法:"+this.getClass().getSimpleName()+"中"+method);
	 * MyBeanUtils.invoke(this, method, parameter, HaloMap.class);
	 * parameter.remove(HaloMap.ADDMETHOD); } }
	 */
	public void initSave(T entity) {
	}

	public final int save(T entity) {
		initSave(entity);
		try {
			getDao().save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public void initUpdate(T entity) {
	}

	public final int update(T entity) {
		initUpdate(entity);
		try {
			getDao().update(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public void initUpdateNotNull(T entity) {
	}

	public final int updateNotNull(T entity) {
		initUpdate(entity);
		try {
			getDao().updateWithNotNull(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public void initUpdateNotNullByHql(T entity) {
	}
 
	public final int updateNotNullByHql(T entity) {
		updateNotNullByHql(entity);
		PK id =getId(entity);
		if(null==id){
			return 0;
		}
		if(id  instanceof String){
			String idStr = String.valueOf(id);
			if(StringUtils.isBlank(idStr)){
				return 0;
			}
		}
		return getDao().updateWithNotNullByHql(entity);
	}
	public void initUpdateNotNullByHql(T entity, HaloMap parameter) {
	}
	public final int updateNotNullByHql(T entity, HaloMap parameter) {
		initUpdateNotNullByHql(entity,  parameter);
		return getDao().updateWithNotNullByHql(entity);
	}

	public final int deleteById(PK id) {
		if (null == id) {
			return 0;
		}
		if (id instanceof String) {
			String idStr = String.valueOf(id);
			if (StringUtils.isBlank(idStr)) {
				return 0;
			}
		}
		getDao().deleteById(id);
		return 1;
	}

	public final int delete(T entity) {
		if (null == entity) {
			return 0;
		}
		getDao().delete(entity);
		return 1;
	}

	public final int deleteByMap(HaloMap parameter) {
		return getDao().deleteByMap(parameter);
	}

	public final T findById(PK id) {
		if (null == id) {
			return create();
		}
		if (id instanceof String) {
			String idStr = String.valueOf(id);
			if (StringUtils.isBlank(idStr)) {
				return create();
			}
		}
		T entity = create();
		try {
			entity = getDao().findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return create();
		}
		return entity;
	}

	public final T checkById(PK id) {
		if (null == id) {
			return create();
		}
		if (id instanceof String) {
			String idStr = String.valueOf(id);
			if (StringUtils.isBlank(idStr)) {
				return create();
			}
		}
		return getDao().checkById(id);
	}

	public final T findFirstByMap(HaloMap parameter) {
		return getDao().findFirstByMap(parameter);
	}

	public final <X> List<X> findListByMap(HaloMap parameter) {
		if (null == parameter) {
			parameter = new HaloMap();
		}
		return getDao().findListByMap(parameter);
	}

	public final Page<T> findPageByMap(Page<T> page, HaloMap parameter) {
		if (null == parameter) {
			parameter = new HaloMap();
		}
		return getDao().findPageByMap(page, parameter);
	}

}
