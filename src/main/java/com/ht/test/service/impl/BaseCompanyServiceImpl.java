package com.ht.test.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.halo.dao.IHaloDao;
import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.service.impl.CURDServiceImpl;
import com.ht.test.dao.IBaseCompanyDao;
import com.ht.test.entity.BaseCompany;
import com.ht.test.service.IBaseCompanyService;
@Service
@Transactional
public class BaseCompanyServiceImpl extends CURDServiceImpl<BaseCompany, String> implements IBaseCompanyService{
   @Resource
	private IBaseCompanyDao baseCompanyDao;
	@Override
	public IHaloDao<BaseCompany, String> getDao() {
		return baseCompanyDao;
	}
	public void initSave(BaseCompany entity){
		entity.setCreateTime(new  Date());
	}
	public void  findListTest(HaloMap parameter){
		parameter.addOrder("createTime_desc");
	}
	@Override
	public BaseCompany create() {
		return new BaseCompany();
	}

 
}
