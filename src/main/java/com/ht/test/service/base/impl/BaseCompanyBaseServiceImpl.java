package com.ht.test.service.base.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.test.dao.BaseCompanyDao;
import com.ht.test.entity.BaseCompany;
import com.ht.test.service.base.IBaseCompanyBaseService;

public  class BaseCompanyBaseServiceImpl  implements IBaseCompanyBaseService{
    @Resource
	private BaseCompanyDao baseCompanyDao;
	@Override
	public BaseCompany findBaseCompanyById(String id) {
		return baseCompanyDao.load(id);
	}
	@Override
	public BaseCompany findBaseCompanyFirst(HaloMap parameter) {
		return baseCompanyDao.findFirstByMap(parameter);
	}
	@Override
	public List<BaseCompany> findBaseCompanyListByMap(HaloMap parameter) {
		return baseCompanyDao.findListByMap(parameter);
	}
	@Override
	public Page<BaseCompany> findBaseCompanyPageByMap(Page<BaseCompany> page,HaloMap parameter) {
		return baseCompanyDao.findPageByMap(page, parameter);
	}
	
	@Override
	public BaseCompany changeBaseCompanyNotNull(BaseCompany entity) {
		entity.setUpdateTime(new Date());
		baseCompanyDao.updateWithNotNull(entity);
		return entity;
	}
	@Override
	public int changeBaseCompanyByMap(BaseCompany entity, HaloMap parameter) {
		entity.setUpdateTime(new Date());
		return baseCompanyDao.updateWithNotNullByHql(entity, parameter);
	}
	@Override
	public void deleteBaseCompanyById(String id) {
		 baseCompanyDao.delete(id);
	}
	@Override
	public int deleteBaseCompanyByMap(HaloMap parameter) {
		return baseCompanyDao.deleteByMap(parameter);
	}
	@Override
	public BaseCompany addBaseCompany(BaseCompany entity) {
		entity.setCreateTime(new  Date());
		entity.setUpdateTime(new  Date());
		entity.setState(0);
		baseCompanyDao.save(entity);
		return entity;
	}
	@Override
	public BaseCompany changeBaseCompany(BaseCompany entity) {
		baseCompanyDao.update(entity);
		return entity;
	}

}
