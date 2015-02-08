package com.ht.test.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.utils.gson.GsonUtils;
import com.ht.test.entity.HaloTest;
import com.ht.utils.junit.BaseDaoTestCase;

public class HaloTestDaoTest extends BaseDaoTestCase{
	@Resource
	private HaloTestDao haloTestDao;
	@Test
	public void testFindListByMap() {
	List<HaloTest> haloTests=	haloTestDao.findListByHaloView(new HaloMap()
	.set("houseId_data", "ab").set("houseName_like", "6").addView("a")
	.set("houseName_eq", "6")
	.addOrder("houseName_desc")
	);
	 System.out.println(GsonUtils.getGsonIn().toJson(haloTests));
	}
}
