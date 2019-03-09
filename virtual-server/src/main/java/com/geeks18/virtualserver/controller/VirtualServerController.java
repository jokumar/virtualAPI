package com.geeks18.virtualserver.controller;

import javax.annotation.Resource;

import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.geeks18.virtualserver.drools.DroolsConfiguration;
import com.geeks18.virtualserver.drools.model.OrderRuleModel;

@Controller
public class VirtualServerController {


	@Resource 
	DroolsConfiguration droolsConfiguration;
	  
	  @PostMapping(value="/calculateFare") 
	  public @ResponseBody OrderRuleModel  calculateFare(@RequestBody OrderRuleModel ordermodel){ 
		  
		  KieSession kieSession = droolsConfiguration.getKieContainer().newKieSession();
		  kieSession.insert(ordermodel);
		  kieSession.fireAllRules();
		  System.out.println("--->"+ordermodel.getStatus());
	  return ordermodel;
	  }
	 
}
