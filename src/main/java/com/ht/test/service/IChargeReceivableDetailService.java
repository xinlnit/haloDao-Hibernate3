package com.ht.test.service;

import java.util.List;

import com.ht.halo.hibernate3.HaloMap;
import com.ht.halo.hibernate3.base.Page;
import com.ht.test.entity.ChargeReceivableDetail;
/**
 * @ClassName: IChargeReceivableDetailService
 * @Description: TODO 应收明细Service
 * @author fengchangyi
 * @date 2014年06月30日 09:17:14 
 */
public interface IChargeReceivableDetailService {
	/**
	 * @Title: findChargeReceivableDetailListByMap
	 * @Description: TODO 查询应收明细列表
	 * @param parameters
	 *            自定义参数
	 * @return
	 */
	public List<ChargeReceivableDetail> findChargeReceivableDetailList(HaloMap parameter);
	public List<ChargeReceivableDetail> findChargeReceivableDetailList(ChargeReceivableDetail entity);
	public Page<ChargeReceivableDetail> findChargeReceivableDetailPage(Page<ChargeReceivableDetail> page,HaloMap parameter);
	public ChargeReceivableDetail findChargeReceivableDetailById(String  id);
	public void updateChargeReceivableDetailNotNull(ChargeReceivableDetail entity);

}
