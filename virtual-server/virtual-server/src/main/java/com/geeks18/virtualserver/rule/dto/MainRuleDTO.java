package com.geeks18.virtualserver.rule.dto;

public class MainRuleDTO {
	private Integer ruleId;
	private String ruleName;
	private String ruleTemplate;
	private String whenRule;
	private String thenRule;

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleTemplate() {
		return ruleTemplate;
	}

	public void setRuleTemplate(String ruleTemplate) {
		this.ruleTemplate = ruleTemplate;
	}

	public String getWhenRule() {
		return whenRule;
	}

	public void setWhenRule(String whenRule) {
		this.whenRule = whenRule;
	}

	public String getThenRule() {
		return thenRule;
	}

	public void setThenRule(String thenRule) {
		this.thenRule = thenRule;
	}
}
