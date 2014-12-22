package com.ht.utils.junit;
 
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
 
@Ignore
@RunWith(MySpringJUnit4ClassRunner.class)   
@ContextConfiguration(locations={"application-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", 
defaultRollback = false) 
@Transactional
public class BaseDaoTestCase extends
AbstractTransactionalJUnit4SpringContextTests { 
 
}