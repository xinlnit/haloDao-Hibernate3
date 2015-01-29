package com.ht.halo.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.HaloViewMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.map.MyHashMap;
import com.ht.halo.hibernate3.map.MyLinkedHashMap;
public interface IHaloDao<T,PK extends Serializable>  {
	       //cud
		   public void save(T entity);
		   public void delete(T entity) ;
		   public void deleteById(PK id) ;
		   public int deleteByMap(HaloMap parameter);
		   public void update(T entity);
		   public void updateWithNotNull(T entity);
		   public int updateWithNotNullByHql(T entity);
		   public int updateWithNotNullByHql(T entity, HaloMap parameter);
		   //R
		   public T findById(PK  id);
		   public T checkById(PK id);
		   public <X> List<X> findListByMap(HaloMap parameter);
		   public <X> List<X> findListByMap(HaloMap parameter, int num);
		   public <X> List<X> findListByMap(HaloMap parameter, int begin, int end);
		   public <X> List<X> findListByEntity(T entity);
		   public T findFirstByMap(HaloMap parameter);
		   public T findUnique(HaloMap parameter);
		   public Page<T> findPageByMap(Page<T> page, HaloMap parameter);
		   //proc
		   public SQLQuery createProcQuery(String procedureName, MyLinkedHashMap parameter);
		   public int executeProc(String procedureName, MyLinkedHashMap parameter);
		   @SuppressWarnings("rawtypes")
		   public List findListByProc(String procedureName, MyLinkedHashMap parameter, Class<?> resultClass);
		   public List<HaloViewMap> findListByProc(String procedureName, MyLinkedHashMap parameter);
		   //hql
		   public Query createQueryByXml(String id, MyHashMap tplMap, MyHashMap parameter);
		   public Query createQueryByXml(String id,MyHashMap parameter);
		   public int executeByHql(String id, MyHashMap tplMap, MyHashMap parameter);
		   public int executeByHql(String id, MyHashMap parameter) ;
		   public <X> List<X>findByHql(String id, MyHashMap tplMap, MyHashMap parameter) ;
		   public <X> List<X> findByHql(String id, MyHashMap parameter) ;
		   public Page<T> findPageByHql(Page<T> page, String id, MyHashMap tplMap, MyHashMap parameter);
		   public Page<T> findPageByHql(Page<T> page, String id, MyHashMap parameter);
		   //sql
		   public SQLQuery createSQLQueryByXml(String id, MyHashMap tplMap, MyHashMap parameter);
		   public SQLQuery createSQLQueryByXml(String id, MyHashMap parameter);
		   public int executeBySql(String id, MyHashMap tplMap, MyHashMap parameter);
		   public int executeBySql(String id, MyHashMap parameter);
		   public T findFirstBySql(String id, MyHashMap tplMap, MyHashMap parameter);
		   public T findUniqueBySql(String id, MyHashMap tplMap, MyHashMap parameter);
		   public <X> List<X> findBySql(String id, MyHashMap tplMap, MyHashMap parameter) ;
		   public <X> List<X> findBySql(String id, MyHashMap parameter);
		   public Page<T> findPageBySql(Page<T> page, String id, MyHashMap tplMap, MyHashMap parameter);
		   public Page<T> findPageBySql(Page<T> page, String id, MyHashMap parameter);
	}

