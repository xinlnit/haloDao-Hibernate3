package com.ht.test.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.test.entity.ViewHalo;
import com.ht.utils.junit.BaseDaoTestCase;

public class HaloViewDaoTest extends BaseDaoTestCase{
	@Resource
    private HaloViewDao haloViewDao;
	@Test
	public void testConvert() {
		/*		Double a=111111111.03;
		System.out.println(a);
	  System.out.println(new Gson().toJson(a));*/
	int a=1;
	Object value="11.001";
	value=ConvertUtils.convert(value,Double.class);
	  System.out.println(ConvertUtils.convert(value,int.class));
	}
	@Test
	public void testFindListWithHaloView() {
	List<ViewHalo> viewHalos =	haloViewDao.findListByHaloView("ViewHalo", new HaloMap()
		.set("houseName", "6-1-1500")
		.set("money", 1.0)
		);
	  for (ViewHalo viewHalo : viewHalos) {
	    	 System.out.println(viewHalo.getHouseName());
	    	 System.out.println(viewHalo.getFlag());
	    	 System.out.println(viewHalo.getMoney());
	   }

	}

}
