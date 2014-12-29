package com.ht.codegen;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ht.codegen.utils.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TypeShortDirective implements TemplateDirectiveModel{
	public static final String PARAM_STR = "str"; 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String str=DirectiveUtils.getString(PARAM_STR, params);
		if(-1!=str.indexOf(".")){
		str=StringUtils.substringAfterLast(str, ".");
		}
		env.getOut().append(str);
	}

}
