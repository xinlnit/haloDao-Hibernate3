package com.ht.codegen.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.codegen.entity.MyEntity;
import com.ht.codegen.entity.ViewTable;
import com.ht.codegen.utils.TableUtil;
import com.ht.halo.hibernate3.HaloMap;

@Service
@Transactional
public class GenEntityService {
	@Resource
     private ViewColumnService viewColumnService;
	@Resource
     private ViewTableService viewTableService;
	 private  MyEntity toEntity(HaloMap parameter){
		ViewTable viewTable =  viewTableService.findViewTable(parameter);
		MyEntity myEntity = new MyEntity();
		myEntity.setEntityId(viewTable.getTableId());
		myEntity.setEntityName(TableUtil.toEntity(viewTable.getTableName()));
		 return null;
	 }
}
