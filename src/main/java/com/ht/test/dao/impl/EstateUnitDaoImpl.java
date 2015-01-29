package com.ht.test.dao.impl;
import org.springframework.stereotype.Repository;
import com.ht.halo.hibernate3.HaloDao;
import com.ht.test.dao.IEstateUnitDao;
import com.ht.test.entity.EstateUnit;
@Repository
public class EstateUnitDaoImpl  extends HaloDao<EstateUnit, String> implements IEstateUnitDao{

}