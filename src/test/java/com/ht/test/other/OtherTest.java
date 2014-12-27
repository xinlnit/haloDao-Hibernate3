package com.ht.test.other;

import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;

public class OtherTest{

	@Test
	public void testConvert() {
		/*		Double a=111111111.03;
		System.out.println(a);
	  System.out.println(new Gson().toJson(a));*/
	Object value="11.001";
	value=ConvertUtils.convert(value,Double.class);
	  System.out.println(ConvertUtils.convert(value,int.class));
	}

}
