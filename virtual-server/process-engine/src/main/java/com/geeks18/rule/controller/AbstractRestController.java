package com.geeks18.rule.controller;

import java.io.Serializable;

import javax.annotation.Resource;

import org.kie.api.runtime.KieSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class AbstractRestController<T,O extends Serializable> {
	O response;
	public AbstractRestController(O resp){
		this.response=resp;
	}
	
	@Resource(name="kieSession")
	KieSession kieSession;
	
	
	@RequestMapping(value = "/process", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody O process(@RequestBody T payload){
		
		System.out.println(payload);
		
	
		
		kieSession.insert(payload);
		kieSession.insert(this.response);
		kieSession.fireAllRules();

//		System.out.println("The discount for the jewellery product "
//				+ response.getPrice() + " is " + response.getStatus());

		return this.response;
		
		
	}
}
