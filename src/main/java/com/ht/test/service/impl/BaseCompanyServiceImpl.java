package com.ht.test.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ht.halo.dao.IHaloDao;
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
		// TODO Auto-generated method stub
		return baseCompanyDao;
	}
 


}
