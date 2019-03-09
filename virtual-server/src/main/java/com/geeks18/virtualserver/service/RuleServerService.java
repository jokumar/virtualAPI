package com.geeks18.virtualserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.geeks18.virtualserver.db.RuleRepository;
import com.geeks18.virtualserver.hibernate.model.MainRuleModel;
import com.geeks18.virtualserver.rule.dto.MainRuleDTO;

@Component
public class RuleServerService {
	@Resource
	RuleRepository ruleRepository;

	
	public Boolean createRule(MainRuleDTO mainRuleDto) {
		MainRuleModel mainRuleModel = new MainRuleModel();
		mainRuleModel.setRuleName(mainRuleDto.getRuleName());
		mainRuleModel.setRuleTemplate(mainRuleDto.getRuleTemplate());
		mainRuleModel.setWhenRule(mainRuleDto.getWhenRule());
		mainRuleModel.setThenRule(mainRuleDto.getThenRule());
		ruleRepository.save(mainRuleModel);
		return true;
	}

	public Boolean updateRule(MainRuleDTO mainRuleDto) {
		Optional<MainRuleModel> mainRuleModel = ruleRepository.findById(mainRuleDto.getRuleId());

		mainRuleModel.get().setRuleName(mainRuleDto.getRuleName());
		mainRuleModel.get().setRuleTemplate(mainRuleDto.getRuleTemplate());
		mainRuleModel.get().setWhenRule(mainRuleDto.getWhenRule());
		mainRuleModel.get().setThenRule(mainRuleDto.getThenRule());
		ruleRepository.save(mainRuleModel.get());
		
		return true;
	}
	
	public List<MainRuleDTO>  getAllRules() {
		List<MainRuleDTO> list=new ArrayList<>();
		ruleRepository.findAll().forEach(x->{
			MainRuleDTO mainRuleDto =new MainRuleDTO();
				mainRuleDto.setRuleName(x.getRuleName());
				mainRuleDto.setRuleId(x.getRuleId());
				mainRuleDto.setWhenRule(x.getWhenRule());
				mainRuleDto.setThenRule(x.getThenRule());
				mainRuleDto.setRuleTemplate(x.getRuleTemplate());
				list.add(mainRuleDto);
		});
		return list;
	}
	public List<MainRuleDTO>  getAllRulesByTemplate(String template) {
		List<MainRuleDTO> list=new ArrayList<>();
		ruleRepository.findAll().forEach(x->{
			MainRuleDTO mainRuleDto =new MainRuleDTO();
			
			if(template.equalsIgnoreCase(x.getRuleTemplate())){
				mainRuleDto.setRuleName(x.getRuleName());
				mainRuleDto.setRuleId(x.getRuleId());
				mainRuleDto.setWhenRule(x.getWhenRule());
				mainRuleDto.setThenRule(x.getThenRule());
				mainRuleDto.setRuleTemplate(x.getRuleTemplate());
				list.add(mainRuleDto);
			}
		});
		
		return list;
	}
}
