package com.ht.utils.junit;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RunWith(MySpringJUnit4ClassRunner.class)   
@ContextConfiguration(locations={"application-context.xml"})

public abstract class BaseServiceTestCase  {
	 @Autowired
	 protected SessionFactory sessionFactory;
	    protected Session session;
	    
 @Before
	    public void openSession()  throws Exception {
	        session = SessionFactoryUtils.getSession(sessionFactory, true);
	        session.setFlushMode(FlushMode.MANUAL);//MANUAL
	        TransactionSynchronizationManager.bindResource(sessionFactory,new SessionHolder(session));
	       
	    
	    }
	    
	  @After
	    public void closeSession()  throws Exception {
	        TransactionSynchronizationManager.unbindResource(sessionFactory);     
	        SessionFactoryUtils.releaseSession(session, sessionFactory);
	    }


}