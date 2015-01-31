package com.ht.test.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.utils.gson.GsonUtils;
import com.ht.test.entity.BaseCompany;
import com.ht.utils.junit.BaseServiceTestCase;

public class BaseCompanyServiceTest extends BaseServiceTestCase{
    @Resource
	 private  IBaseCompanyService baseCompanyService;
	@Test
	public void testFindBaseCompanyListByMap() {
		//BaseCompany entity =new BaseCompany();
	//baseCompanyService.save(entity);
	  List<BaseCompany> basweBaseCompanies= 	
			  baseCompanyService.findListByMap(new HaloMap());
		  System.out.println(GsonUtils.getGsonIn().toJson(basweBaseCompanies));
		
	}
	@Test
	public void testFindBaseCompanyById() {
	  BaseCompany baseCompany= 	
			  baseCompanyService.findById("4028805e49abcb1d0149abd0585a0000");
		  System.out.println(GsonUtils.getGsonIn().toJson(baseCompany));
	}


}
