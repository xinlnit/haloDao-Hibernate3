package com.ht.utils.junit;

import java.io.FileNotFoundException;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

public class MySpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {

	static {
		try {
			String packageName = MySpringJUnit4ClassRunner.class.getPackage().getName();
			packageName=packageName.replaceAll("\\.", "/");
			Log4jConfigurer.initLogging("classpath:"+packageName+"/log4j.properties");
		} catch (FileNotFoundException ex) {
			System.err.println("Cannot Initialize log4j");
		}
	}
	
	public MySpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

}