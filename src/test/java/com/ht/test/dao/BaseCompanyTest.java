package com.ht.test.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.utils.gson.GsonUtils;
import com.ht.test.entity.BaseCompany;
import com.ht.utils.junit.BaseDaoTestCase;

public class BaseCompanyTest extends BaseDaoTestCase{

	@Resource
	private IBaseCompanyDao baseCompanyDao;
	@Test
	public void testFindListByMap() {
	   System.out.println(System.currentTimeMillis());
		List<BaseCompany> baseCompanies = baseCompanyDao.findListByMap(new HaloMap()
		.set("code_not", 1)
		.set("state_eq", "3.0")
		//.set("companyId_eq", "4028805e49abcb1d0149abd0585a0000")
		.set("createTime_lt", "2015-01")
		);
		 System.out.println(System.currentTimeMillis());
		System.out.println(GsonUtils.format(GsonUtils.getGsonIn().toJson(baseCompanies)));
	}
	@Test
	public void testFindListByPage() {
		 Page<BaseCompany> page =new Page<BaseCompany>(3, 1);
		 page=baseCompanyDao.findPageByMap(new Page<BaseCompany>(3, 1), new HaloMap()
		 .set("code_not", 1)
		 .addGroup("code")
		 );
		System.out.println(GsonUtils.format(GsonUtils.getGsonIn("entities").toJson(page)));
	}
	@Test
	public void testDelete() {
		baseCompanyDao.deleteByMap(null);
		baseCompanyDao.deleteByMap(new HaloMap());
		baseCompanyDao.deleteByMap(new HaloMap().set("code", "1"));
	//	baseCompanyDao.deleteByMap(new HaloMap().set("code_eq", "1"));
	}
	@Test
	public void testUpdate() {
		baseCompanyDao.updateWithNotNullByHql(new BaseCompany()
		.setShortName("ttt"),new HaloMap().set("type_eq", 0));//.setCompanyId("4028805e49abcb1d0149abd0585a0000")
	}

}
