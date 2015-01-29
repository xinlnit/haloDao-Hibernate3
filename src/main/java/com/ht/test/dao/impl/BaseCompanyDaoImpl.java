package com.ht.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.ht.halo.hibernate3.HaloDao;
import com.ht.test.dao.IBaseCompanyDao;
import com.ht.test.entity.BaseCompany;
@Repository
public class BaseCompanyDaoImpl  extends HaloDao<BaseCompany, String> implements IBaseCompanyDao{

}
