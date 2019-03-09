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

	public KieContainer getKieContainer() {
	
		if (kieContainer == null) {
			kieContainer = setKieContainer();
		}
		return kieContainer;
	}

	public void refreshKieContainer() {
		this.kieContainer = null;
	}

	private KieContainer setKieContainer() {
		KieServices kieServices = KieServices.Factory.get();

		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		HashMap<String, Object> data=new HashMap<>();
		List<MainRuleDTO> list = ruleServerService.getAllRulesByTemplate("sample");
		StringBuilder str=new StringBuilder("package com.test;\n dialect \"mvel\" \n ");
		list.forEach(x -> {
			System.out.println("----x>" + x.getWhenRule());
			System.out.println("----y>" + x.getThenRule());
			str.append(applyRuleTemplate(new OrderRuleModel(), x.getWhenRule(), x.getThenRule(),x.getRuleId()));

		});

		
		
		/*ResultSetGenerator converter = new ResultSetGenerator();

		String sql = "SELECT whenCondition,thenCondition  FROM MAINRULE";
		
		try {
			Connection connection = dataSource.getConnection();
			 Statement sta = connection.createStatement();
			 ResultSet rs = sta.executeQuery(sql);
			 String drl = converter.compile(rs, getRulesStream());
			 kieFileSystem.write("src/main/resources/rule.drl", drl);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	/*	jdbcTemplate.query(sql, new ResultSetExtractor<List<MainRuleModel>>() {

			
			public List<MainRuleModel> extractData(ResultSet rs) throws SQLException, DataAccessException {
				String drl=converter.compile(rs,getRulesStream());
				System.out.println("----->"+drl);
				kieFileSystem.write("src/main/resources/rule.drl", drl);
				return null;
			}
		});
*/
		System.out.println(str.toString());
		
		
		 
		 System.out.println("-----------------"+ str);
		
		
		kieFileSystem.write("src/main/resources/rule.drl", str.toString());
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();
		KieModule kieModule = kieBuilder.getKieModule();
		return kieServices.newKieContainer(kieModule.getReleaseId());
	}

	
	private static InputStream getRulesStream()  {

        try {
			return new FileInputStream("C:/Users/joykumar/Documents/My POC/Virtualized Server/virtual-server/virtual-server/src/main/resources/template.drt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;

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