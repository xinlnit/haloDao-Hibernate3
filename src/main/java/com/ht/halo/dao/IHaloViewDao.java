package com.ht.halo.dao;

import java.util.List;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;


public interface IHaloViewDao<T> {
	public <X> List<X> findListByHaloView(HaloMap parameter);
	public T  findFirstByHaloView(HaloMap parameter);
	public <X> List<X>   findListByHaloView(HaloMap parameter,int begin,int end) ;
	public <X> List<X>  findListByHaloView(String viewName, HaloMap parameter,int num);
	public T  findUniqueByHaloView(HaloMap parameter);
	public Page<T> findPageByHaloView( Page<T> page, HaloMap parameter);	
}
