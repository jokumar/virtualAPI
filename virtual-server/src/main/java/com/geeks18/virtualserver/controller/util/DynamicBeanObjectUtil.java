package com.geeks18.virtualserver.controller.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.geeks18.virtualserver.constants.VirtualServerConstant;
import com.geeks18.virtualserver.drools.model.GenericRuleModel;
import com.geeks18.virtualserver.drools.model.OrderRuleModel;

public class DynamicBeanObjectUtil {
	

	public static GenericRuleModel  getInstantiatedBeans(OrderRuleModel model) throws Exception{
		File file=new File(VirtualServerConstant.SOURCE_FILE);
		  URL url = file.toURI().toURL(); 
			URL[] urls = new URL[]{url}; 
				
	                //load this folder into Class loader
			ClassLoader cl = new URLClassLoader(urls); 

	                //load the Address class in 'c:\\other_classes\\'
		    Object paramsObj[] = {model.getOrderId()};
		    Class params[] = {String.class};
		    
			Class  cls = cl.getParent().loadClass("com.geeks18.virtualserver.drools.model.Order");		
			GenericRuleModel  iClass =	(GenericRuleModel) cls.newInstance();
			Method thisMethod = cls.getDeclaredMethod("setOrderId", String.class);
		      thisMethod.invoke(iClass, model.getOrderId().toString());
		      
		     return  iClass;
	}
}
