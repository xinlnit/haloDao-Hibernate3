package com.ht.test.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ht.halo.dao.IHaloDao;
import com.ht.halo.service.impl.CURDServiceImpl;
import com.ht.test.dao.IEstateUnitDao;
import com.ht.test.entity.EstateUnit;
import com.ht.test.service.IEstateUnitService;
@Service
@Transactional
public class EstateUnitServiceImpl extends CURDServiceImpl<EstateUnit, String> implements IEstateUnitService{
   @Resource
	private IEstateUnitDao estateUnitDao;
	@Override
	public IHaloDao<EstateUnit, String> getDao() {
		return estateUnitDao;
	}
	@Override
	public EstateUnit create() {
		// TODO Auto-generated method stub
		return new EstateUnit();
	}
}    