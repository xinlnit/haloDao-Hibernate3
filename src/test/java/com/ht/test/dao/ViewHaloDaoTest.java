package com.ht.test.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.test.entity.ViewHalo;
import com.ht.utils.junit.BaseDaoTestCase;

public class ViewHaloDaoTest extends BaseDaoTestCase{
	@Resource
    private ViewHaloDao viewHaloDao;
	@Test
	public void testConvert() {
		/*		Double a=111111111.03;
		System.out.println(a);
	  System.out.println(new Gson().toJson(a));*/
	Object value="11.001";
	value=ConvertUtils.convert(value,Double.class);
	  System.out.println(ConvertUtils.convert(value,int.class));
	}
	@Test
	public void testFindListWithHaloView() {
	List<ViewHalo> viewHalos =	viewHaloDao.findListByHaloView( new HaloMap()
		.set("houseName", "6-1-1500")
		.set("money", 1.0)
		);
	  for (ViewHalo viewHalo : viewHalos) {
	    	 System.out.println(viewHalo.getHouseName());
	    	 System.out.println(viewHalo.getFlag());
	    	 System.out.println(viewHalo.getMoney());
	   }
	
	}
	@Test
	public void testFindPageByHaloView() {
	Page<ViewHalo> page =	new Page<ViewHalo>(3, 1);
	page =	viewHaloDao.findPageByHaloView(page, new HaloMap()
		.set("houseName", "6-1-1500")
		.set("money", 1.0)
		);
	  for (ViewHalo viewHalo : page.getEntities()) {
	    	 System.out.println(viewHalo.getHouseName());
	    	 System.out.println(viewHalo.getFlag());
	    	 System.out.println(viewHalo.getMoney());
	   }
	
	}
	@Test
	public void testFindByHaloView() {

	ViewHalo viewHalo =	viewHaloDao.findByHaloView( new HaloMap()
		.set("houseName", "6-1-1500")
		.set("money", 1.0)
		);
	    	 System.out.println(viewHalo.getHouseName());
	    	 System.out.println(viewHalo.getFlag());
	    	 System.out.println(viewHalo.getMoney());
	
	
	}
	@Test
	public void testFindByHaloViewNum() {
		List<ViewHalo> viewHalos =	viewHaloDao.findListByHaloView( new HaloMap()
		.set("houseName", "6-1-1500")
		.set("money", 1.0),2
		);
	  for (ViewHalo viewHalo : viewHalos) {
	    	 System.out.println(viewHalo.getHouseName());
	    	 System.out.println(viewHalo.getFlag());
	    	 System.out.println(viewHalo.getMoney());
	   }
	}
	

}
