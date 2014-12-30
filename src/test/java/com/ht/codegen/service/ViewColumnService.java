package com.ht.codegen.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.codegen.dao.ViewColumnDao;
import com.ht.codegen.entity.ViewColumn;
import com.ht.halo.hibernate3.HaloMap;

@Service
@Transactional
public class ViewColumnService {
	@Resource
	private ViewColumnDao viewColumnDao;
	public List<ViewColumn> findViewColumnList(HaloMap parameter){
		List<ViewColumn> viewColumns = viewColumnDao
				.findListByHaloView("codegen.ViewColumn", parameter);
		return viewColumns;
	}
}
