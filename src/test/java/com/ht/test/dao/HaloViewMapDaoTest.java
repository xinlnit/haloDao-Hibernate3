package com.ht.test.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.HaloViewMap;
import com.ht.utils.junit.BaseDaoTestCase;

public class HaloViewMapDaoTest extends BaseDaoTestCase{
	@Resource
    private HaloViewMapDao haloViewMapDao;
	@Test
	public void testFindListWithHaloViewToMap() {
		List<HaloViewMap> viewHaloViewMaps =	haloViewMapDao.findListByHaloView("ViewHalo", new HaloMap()
		.set("houseName", "6-1-1500")
		.set("money", 1.0)
		);
		for (HaloViewMap haloViewMap : viewHaloViewMaps) {
			System.out.println(haloViewMap.getDate("createDate"));
			System.out.println(haloViewMap.getBigDecimal("money"));
		}
	}
	
    
}
