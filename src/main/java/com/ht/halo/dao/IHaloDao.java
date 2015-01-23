package com.ht.halo.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
public interface IHaloDao<T,PK extends Serializable> {
		   public void save(T entity);
		   public void deleteByEntity(T entity) ;
		   public void deleteById(PK id) ;
		   public int deleteByMap(HaloMap parameter);
		   public void update(T entity);
		   public void updateWithNotNull(T entity);
		   public int updateWithNotNullByHql(T entity);
		   public int updateWithNotNullByHql(T entity, HaloMap parameter);
		   public T getById(PK  id);
		   public T checkById(PK id);
		   public <X> List<X> findListByMap(HaloMap parameter);
		   public <X> List<X> findListByMap(HaloMap parameter, int num);
		   public <X> List<X> findListByMap(HaloMap parameter, int begin, int end);
		   public <X> List<X> findListByEntity(T entity);
		   public T findFirstByMap(HaloMap parameter);
		   public T findUnique(HaloMap parameter);
		   public Page<T> findPageByMap(Page<T> page, HaloMap parameter);
		   public SQLQuery createProcQuery(String procedure, Map<String, ?> parameter);
		   public <X> List<X> findListByProc(String procedureName, Map<String, ?> parameter);
		   public SQLQuery CreateSqlQueryByHaloView(String viewName, HaloMap parameter);
		   
	}

