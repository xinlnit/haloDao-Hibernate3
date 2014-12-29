package com.ht.codegen.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.codegen.dao.ViewTableDao;
import com.ht.codegen.entity.ViewTable;
import com.ht.halo.hibernate3.HaloMap;

@Service
@Transactional
public class ViewTableService {
	@Resource
    private  ViewTableDao viewTableDao;
	
	public ViewTable findViewTableByName(String tableName){
	List<ViewTable> viewTables =	viewTableDao.findListByHaloView("codegen.ViewTable", new HaloMap()
		.set("tableName", tableName));
	  if(viewTables.size()>0){
		return viewTables.get(0);
	  }
	  return null;
	}
    
}
