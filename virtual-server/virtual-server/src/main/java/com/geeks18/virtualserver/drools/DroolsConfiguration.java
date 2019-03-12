package com.geeks18.virtualserver.drools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.drools.template.ObjectDataCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.geeks18.virtualserver.constants.VirtualServerConstant;
import com.geeks18.virtualserver.drools.model.GenericRuleModel;
import com.geeks18.virtualserver.rule.dto.MainRuleDTO;
import com.geeks18.virtualserver.service.RuleServerService;

@Component
public class DroolsConfiguration {

	@Resource
	RuleServerService ruleServerService;
	@Resource
	DataSource dataSource;


	@Autowired
	JdbcTemplate jdbcTemplate;



	public void createDrl(String requestName,String responseName) throws IOException {

		List<MainRuleDTO> list = ruleServerService.getAllRulesByTemplate("sample");
		StringBuilder str=new StringBuilder("package com.test;\n dialect \"mvel\" \n ");
		list.forEach(x -> {
			str.append(applyRuleTemplate(requestName,responseName, x.getWhenRule(), x.getThenRule(),x.getRuleId()));

		});
		String source=VirtualServerConstant.SOURCE_FILE_DRL + VirtualServerConstant.DRL_FILE_NAME ;
		File sourceFile = new File(source);
		FileWriter writer = new FileWriter(sourceFile);
		writer.write(str.toString());
		writer.flush();
		writer.close();
		/*kieFileSystem.write("src/main/resources/rule.drl", str.toString());
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();
		KieModule kieModule = kieBuilder.getKieModule();
		return kieServices.newKieContainer(kieModule.getReleaseId());*/
	}

	

	static private String applyRuleTemplate(String requestName,String responseName, String when_rule, String then_rule,Integer ruleId) {

		Map<String, Object> data = new HashMap<String, Object>();
		ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();
		data.put("rule_name", ruleId);
		data.put("when_rule", when_rule);
		data.put("then_rule", then_rule);
		data.put("objectType", requestName);
		data.put("responseType", responseName);
		return objectDataCompiler.compile(Arrays.asList(data),
				Thread.currentThread().getContextClassLoader().getResourceAsStream("template.drt"));
	}
}