package com.geeks18.virtualserver.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.geeks18.virtualserver.controller.util.RuleServerUtil;

@RestController
@RequestMapping("/frontend/parse")
public class FrontEndController {

	@Autowired
	private RuleServerUtil ruleServerUtil;
	
	@RequestMapping(value = "/process", method = RequestMethod.POST, consumes = "text/plain")
	public void process(@RequestBody String payload){		
		ruleServerUtil.convertIncomingFrontEndRequest(payload);		

	}

}
