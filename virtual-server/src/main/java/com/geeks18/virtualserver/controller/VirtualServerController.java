package com.geeks18.virtualserver.controller;

import javax.annotation.Resource;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.geeks18.virtualserver.drools.DroolsConfiguration;
import com.geeks18.virtualserver.drools.model.OrderRuleModel;

@Controller
public class VirtualServerController {


	@Resource 
	DroolsConfiguration droolsConfiguration;
	  
	  @GetMapping(value="/calculateFare") 
	  public @ResponseBody Long  calculateFare(){ 
		  OrderRuleModel model=new OrderRuleModel();
		  model.setOrderId(10);
		  KieSession kieSession = droolsConfiguration.getKieContainer().newKieSession();
		  kieSession.insert(model);
		  kieSession.fireAllRules();
		  System.out.println("--->"+model.getStatus());
	  return new Long(1);
	  }
	 
}
