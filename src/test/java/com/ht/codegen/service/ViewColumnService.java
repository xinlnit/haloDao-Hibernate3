package com.ht.codegen.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.codegen.dao.HaloViewColumnDao;
import com.ht.codegen.entity.HaloViewColumn;
import com.ht.halo.hibernate3.HaloMap;

@Service
@Transactional
public class ViewColumnService {
	@Resource
	private HaloViewColumnDao viewColumnDao;
	public List<HaloViewColumn> findViewColumnList(HaloMap parameter){
		List<HaloViewColumn> viewColumns = viewColumnDao
				.findListByHaloView( parameter);
		return viewColumns;
	}
}
