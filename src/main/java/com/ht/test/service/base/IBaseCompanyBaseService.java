package com.ht.test.service.base;

import java.util.List;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.test.entity.BaseCompany;

/**
 * TODO 物业企业BaseService
 * 
 * @author fengchangyi@haitao-tech.com
 * @date 2014-12-31 下午2:17:33
 */
public interface IBaseCompanyBaseService {
	/**
	 * TODO 根据Id查找物业企业
	 * 
	 * @param id
	 * @return 物业企业
	 */
	public BaseCompany findBaseCompanyById(String id);

	/**
	 * TODO 查找第一条物业企业记录
	 * 
	 * @param HaloMap
	 * @return 物业企业
	 */
	public BaseCompany findBaseCompanyFirst(HaloMap parameter);

	/**
	 * TODO 根据HaloMap查找物业企业列表
	 * 
	 * @param parameter
	 * @return 物业企业列表
	 */
	public List<BaseCompany> findBaseCompanyListByMap(HaloMap parameter);

	/**
	 * TODO 根据HaloMap分页查找物业企业
	 * 
	 * @param page
	 * @param HaloMap
	 * @return 物业企业列表页
	 */
	public Page<BaseCompany> findBaseCompanyPageByMap(Page<BaseCompany> page, HaloMap parameter);
	/**
	 *  TODO 添加物业企业
	 * @param 物业企业实体 
	 * @return 物业企业实体 
	 */
	public BaseCompany addBaseCompany(BaseCompany entity);
	/**
	 *  TODO  修改物业企业
	 * @param 物业企业实体 
	 * @return 物业企业实体
	 */
	public BaseCompany changeBaseCompany(BaseCompany entity);
	/**
	 * TODO 修改物业企业(不为null的属性)
	 * 
	 * @param 物业企业实体
	 * @return 物业企业实体
	 */
	public BaseCompany changeBaseCompanyNotNull(BaseCompany entity);

	/**
	 * TODO 根据HaloMap查询修改物业企业(不为null的属性)
	 * 
	 * @param 物业企业实体
	 * @param HaloMap
	 * @return 修改行数
	 */
	public int changeBaseCompanyByMap(BaseCompany entity, HaloMap parameter);

	/**
	 * TODO 根据Id删除物业企业
	 * 
	 * @param id
	 */
	public void deleteBaseCompanyById(String id);

	/**
	 * TODO 根据HaloMap查询删除物业企业记录
	 * 
	 * @param HaloMap
	 * @return 删除的行数
	 */
	public int deleteBaseCompanyByMap(HaloMap parameter);
}
