package com.ht.test.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.utils.gson.GsonUtils;
import com.ht.test.entity.BaseCompany;
import com.ht.utils.junit.BaseDaoTestCase;

public class BaseCompanyTest extends BaseDaoTestCase{
	private static final Log logger = LogFactory.getLog(BaseCompanyTest.class);
	@Resource
	private BaseCompanyDao baseCompanyDao;
	@Test
	public void testQueryListByMap() {
	   System.out.println(System.currentTimeMillis());
		List<BaseCompany> baseCompanies = baseCompanyDao.findListByMap(new HaloMap()
		.set("companyId", "4028805e49abcb1d0149abd0585a0000"));
		 System.out.println(System.currentTimeMillis());
		logger.info(GsonUtils.getGsonIn().toJson(baseCompanies));
	}
}
