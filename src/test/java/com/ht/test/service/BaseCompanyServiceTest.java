package com.ht.test.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.ht.halo.hibernate3.utils.gson.GsonUtils;
import com.ht.test.entity.BaseCompany;
import com.ht.utils.junit.BaseServiceTestCase;

public class BaseCompanyServiceTest extends BaseServiceTestCase{
    @Resource
	 private  IBaseCompanyService baseCompanyService;
	@Test
	public void testFindBaseCompanyListByMap() {
	 /* List<BaseCompany> basweBaseCompanies= 	
			  baseCompanyService.findBaseCompanyListByMap(null);
		  System.out.println(HtGson.getGsonIn().toJson(basweBaseCompanies));*/
		
	}
	@Test
	public void testFindBaseCompanyById() {
	  BaseCompany baseCompany= 	
			  baseCompanyService.findBaseCompanyById("4028805e49abcb1d0149abd0585a0000");
		  System.out.println(GsonUtils.getGsonIn().toJson(baseCompany));
		
	}


}
