package com.ht.test.service.base.impl;

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
		baseCompanyDao.updateWithNotNull(entity);
		return entity;
	}
	@Override
	public int changeBaseCompanyByMap(BaseCompany entity, HaloMap parameter) {
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

}
