package com.ht.codegen;

import java.io.IOException;
import java.util.Map;

import com.ht.codegen.annotations.FieldInfo;
import com.ht.codegen.utils.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class FieldInfoDirective implements TemplateDirectiveModel{
	public static final String PARAM_field = "field"; 
	public static final String PARAM_entity = "entity"; 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		 Class bean;
		 String result="";
		 String field=DirectiveUtils.getString(PARAM_field, params);
		 String entity=DirectiveUtils.getString(PARAM_entity, params);
		try {
			bean = Class.forName(entity);
			FieldInfo fiedInfo=	bean.getDeclaredField(field).getAnnotation(FieldInfo.class);
			if(null!=fiedInfo){
				result=fiedInfo.desc();
			}else{
				result=field;
			}
			env.getOut().append(result);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
