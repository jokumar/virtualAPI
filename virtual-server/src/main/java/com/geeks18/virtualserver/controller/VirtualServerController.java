package com.geeks18.virtualserver.controller;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.annotation.Resource;

import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.geeks18.virtualserver.constants.VirtualServerConstant;
import com.geeks18.virtualserver.controller.util.DynamicBeanObjectUtil;
import com.geeks18.virtualserver.drools.DroolsConfiguration;
import com.geeks18.virtualserver.drools.model.GenericRuleModel;
import com.geeks18.virtualserver.drools.model.OrderRuleModel;


@Controller
public class VirtualServerController {


	@Resource 
	DroolsConfiguration droolsConfiguration;
	  
	  @PostMapping(value="/calculateFare") 
	  public @ResponseBody Object  calculateFare(@RequestBody OrderRuleModel ordermodel) throws Exception{ 
		
			
			      
		  GenericRuleModel iClass= DynamicBeanObjectUtil.getInstantiatedBeans(ordermodel);
		  
		  KieSession kieSession = droolsConfiguration.getKieContainer(iClass).newKieSession();
		  kieSession.insert(iClass);
		  kieSession.fireAllRules();
	  return iClass;
	  }
	 
}
