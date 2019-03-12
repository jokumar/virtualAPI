package com.geeks18.virtualserver.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.geeks18.virtualserver.rule.dto.MainRuleDTO;
import com.geeks18.virtualserver.service.RuleServerService;

/**
 * 
 * @author joykumar Controller to persist the Rule Engine in the db and generate
 *         a pojo .
 */
@RestController
@RequestMapping("/virtualServer/rules")
public class RulesServerController   {

/*	@Resource 
	DroolsConfiguration droolsConfiguration;*/
	@Resource
	RuleServerService ruleServerService;

	@PostMapping(value = "/createRule", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Boolean createRule(@RequestBody MainRuleDTO mainRuleDto) {
		 ruleServerService.createRule(mainRuleDto);
			// droolsConfiguration.refreshKieContainer(); CALL THE API TO REFRESH
		// droolsConfiguration.getKieContainer();
		 return true;
	}
	
	@PostMapping(value = "/createRules", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Boolean createRule(@RequestBody List<MainRuleDTO> list) {
		 ruleServerService.createRules(list);
		// droolsConfiguration.refreshKieContainer(); CALL THE API TO REFRESH
		// droolsConfiguration.getKieContainer();
		 return true;
	}

	@PostMapping(value = "/updateRule", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Boolean updateRule(@RequestBody MainRuleDTO mainRuleDto) {
		 ruleServerService.createOrUpdate(mainRuleDto);
			// droolsConfiguration.refreshKieContainer(); CALL THE API TO REFRESH
		// droolsConfiguration.getKieContainer(iClass);
		 return true;
	}
	@GetMapping(value = "/getAllRule/{template}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<MainRuleDTO>  getAllRule(@PathVariable("template") String template) {
		if(StringUtils.isEmpty(template)){
			return ruleServerService.getAllRulesByTemplate(template);
		}else{
			return ruleServerService.getAllRules();
		}
		
	}
	

	@GetMapping(value = "/deleteAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Boolean  deleteAllRules() {
			return ruleServerService.deleteAllRules();
		
	}
	

}
