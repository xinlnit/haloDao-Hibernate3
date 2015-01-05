package com.ht.codegen.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.codegen.dao.HaloViewCommentDao;
import com.ht.codegen.entity.HaloViewComment;

@Service
@Transactional
public class HaloViewCommentService {
	@Resource
	 private HaloViewCommentDao haloViewCommentDao;
	  public String getViewCommentByName(String name){
	    //name=TableUtil.toHql(name);
		HaloViewComment haloViewComment =  haloViewCommentDao.get(name);
		return haloViewComment.getComment();
		  
	  }

}
