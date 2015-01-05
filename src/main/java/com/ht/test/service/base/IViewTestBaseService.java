package com.ht.test.service.base;

import java.util.List;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.test.entity.ViewTest;

/**
 * TODO 测试BaseService
 * 
 * @author fengchangyi
 * @date 2015年01月05日 08:54:53 
 */
public interface IViewTestBaseService {
	/**
	 * TODO 根据Id查找测试
	 * 
	 * @param id
	 * @return 测试
	 */
	public ViewTest findViewTestById(String id);

	/**
	 * TODO 查找第一条测试记录
	 * 
	 * @param HaloMap
	 * @return 测试
	 */
	public ViewTest findViewTestFirst(HaloMap parameter);

	/**
	 * TODO 根据HaloMap查找测试列表
	 * 
	 * @param parameter
	 * @return 测试列表
	 */
	public List<ViewTest> findViewTestListByMap(HaloMap parameter);

	/**
	 * TODO 根据HaloMap分页查找测试
	 * 
	 * @param page
	 * @param HaloMap
	 * @return 测试列表页
	 */
	public Page<ViewTest> findViewTestPageByMap(Page<ViewTest> page, HaloMap parameter);

}
