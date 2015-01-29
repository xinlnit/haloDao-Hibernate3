package com.ht.codegen.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.codegen.dao.HaloViewTableDao;
import com.ht.codegen.entity.HaloViewTable;
import com.ht.halo.hibernate3.HaloMap;

@Service
@Transactional
public class ViewTableService {
	@Resource
    private  HaloViewTableDao viewTableDao;
	
	public HaloViewTable findViewTable(HaloMap parameter){
	List<HaloViewTable> viewTables =	viewTableDao.findListByHaloView(
			parameter);
	  if(viewTables.size()>0){
		return viewTables.get(0);
	  }
	  return null;
	}

    
}
