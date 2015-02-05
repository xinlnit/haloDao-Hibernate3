package com.ht.test.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.HaloViewMap;
import com.ht.halo.hibernate3.utils.gson.GsonUtils;
import com.ht.utils.junit.BaseDaoTestCase;

public class TestHaloViewMapDao extends BaseDaoTestCase{
	@Resource
	private IHaloViewMapDao haloViewMapDao;
	@Test
	public void testFindListByMap() {
		List<HaloViewMap> haloTests=	haloViewMapDao.findListByHaloView(new HaloMap()
		.set("houseId_data", "ab").set("houseName_like", "6")
		.set("houseName_eq", "6")
		.addOrder("houseName_desc")
		);
		 System.out.println(GsonUtils.getGsonIn().toJson(haloTests));
	}
	
}
