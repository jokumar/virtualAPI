package com.geeks18.virtualserver.drools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.geeks18.virtualserver.drools.model.GenericRuleModel;
import com.geeks18.virtualserver.drools.model.OrderRuleModel;
import com.geeks18.virtualserver.rule.dto.MainRuleDTO;
import com.geeks18.virtualserver.service.RuleServerService;

@Component
public class DroolsConfiguration {

	@Resource
	RuleServerService ruleServerService;
	@Resource
	DataSource dataSource;

	private KieContainer kieContainer;
	
	

	@Autowired
	JdbcTemplate jdbcTemplate;

	public KieContainer getKieContainer(GenericRuleModel obj) {
	
		if (kieContainer == null) {
			kieContainer = createKieContainer(obj);
		}
		return kieContainer;
	}

	public void refreshKieContainer() {
		this.kieContainer = null;
	}

	private KieContainer createKieContainer(GenericRuleModel obj) {
		KieServices kieServices = KieServices.Factory.get();

		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		List<MainRuleDTO> list = ruleServerService.getAllRulesByTemplate("sample");
		StringBuilder str=new StringBuilder("package com.test;\n dialect \"mvel\" \n ");
		list.forEach(x -> {
			str.append(applyRuleTemplate(obj, x.getWhenRule(), x.getThenRule(),x.getRuleId()));

		});
		
		kieFileSystem.write("src/main/resources/rule.drl", str.toString());
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();
		KieModule kieModule = kieBuilder.getKieModule();
		return kieServices.newKieContainer(kieModule.getReleaseId());
	}

	

	static private String applyRuleTemplate(GenericRuleModel model, String when_rule, String then_rule,Integer ruleId) {

		Map<String, Object> data = new HashMap<String, Object>();
		ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();
		data.put("rule_name", ruleId);
		data.put("when_rule", when_rule);
		data.put("then_rule", then_rule);
		data.put("objectType", model.getClass().getName());
		return objectDataCompiler.compile(Arrays.asList(data),
				Thread.currentThread().getContextClassLoader().getResourceAsStream("template.drt"));
	}
}