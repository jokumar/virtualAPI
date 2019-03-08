package com.geeks18.virtualserver.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="MAINRULE")
public class MainRuleModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "RULEID",  nullable = false)
	private Integer ruleId;
	
	@Column(name = "RULENAME")
	private String ruleName;
	
	@Column(name = "RULETEMPLATE")
	private String ruleTemplate;
	
	@Lob
	@Column(name = "WHENCONDITION")
	private String whenRule;
	
	@Lob
	@Column(name = "THENCONDITION")
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
