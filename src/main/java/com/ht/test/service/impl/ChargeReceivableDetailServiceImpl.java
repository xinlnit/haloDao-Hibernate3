package com.ht.test.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.halo.hibernate3.base.Page;
import com.ht.halo.hibernate3.map.HaloMap;
import com.ht.test.dao.ChargeReceivableDetailDao;
import com.ht.test.entity.ChargeReceivableDetail;
import com.ht.test.service.IChargeReceivableDetailService;
@Service
@Transactional
public class ChargeReceivableDetailServiceImpl implements IChargeReceivableDetailService {
   @Resource
	private ChargeReceivableDetailDao chargeReceivableDetailDao;
	@Override
	public List<ChargeReceivableDetail> findChargeReceivableDetailList(HaloMap parameter) {
		return chargeReceivableDetailDao.findListByMap(parameter);
	}
	@Override
	public List<ChargeReceivableDetail> findChargeReceivableDetailList(ChargeReceivableDetail entity) {
		return chargeReceivableDetailDao.findListByEntity(entity);
	}
	@Override
	public Page<ChargeReceivableDetail> findChargeReceivableDetailPage(Page<ChargeReceivableDetail> page, HaloMap parameter) {
		return chargeReceivableDetailDao.findPageByMap(page, parameter);
	}
	@Override
	public void updateChargeReceivableDetailNotNull(ChargeReceivableDetail entity) {
		chargeReceivableDetailDao.updateWithNotNull(entity);
	}
	@Override
	public ChargeReceivableDetail findChargeReceivableDetailById(String id) {
		return chargeReceivableDetailDao.get(id);
	}

}
