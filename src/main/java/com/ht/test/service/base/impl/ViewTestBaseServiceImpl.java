package com.ht.test.service.base.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.test.dao.ViewTestDao;
import com.ht.test.entity.ViewTest;
import com.ht.test.service.base.IViewTestBaseService;

public  class ViewTestBaseServiceImpl  implements IViewTestBaseService{
    @Resource
	private ViewTestDao viewTestDao;
	@Override
	public ViewTest findViewTestById(String id) {
		return viewTestDao.load(id);
	}
	@Override
	public ViewTest findViewTestFirst(HaloMap parameter) {
		return viewTestDao.findFirstByMap(parameter);
	}
	@Override
	public List<ViewTest> findViewTestListByMap(HaloMap parameter) {
		return viewTestDao.findListByMap(parameter);
	}
	@Override
	public Page<ViewTest> findViewTestPageByMap(Page<ViewTest> page,HaloMap parameter) {
		return viewTestDao.findPageByMap(page, parameter);
	}
}
