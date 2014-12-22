package com.ht.halo.hibernate3.feemarker;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class NumberToCNDirective implements TemplateDirectiveModel {
	public static final String PARAM_NUMBER = "num"; 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		 String number=DirectiveUtils.getString(PARAM_NUMBER, params);
		String numberCn =
				NumberToCN.number2CNMontrayUnit(new BigDecimal(number));
		env.getOut().append(numberCn);
	}


}
