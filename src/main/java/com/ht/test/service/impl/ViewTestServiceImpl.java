package com.ht.test.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.test.service.IViewTestService;
import com.ht.test.service.base.impl.ViewTestBaseServiceImpl;
@Service
@Transactional
public class ViewTestServiceImpl extends ViewTestBaseServiceImpl implements IViewTestService{
 
}