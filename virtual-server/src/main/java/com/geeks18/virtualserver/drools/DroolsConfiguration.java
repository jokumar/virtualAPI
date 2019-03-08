package com.geeks18.virtualserver.drools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.geeks18.virtualserver.drools.model.GenericRuleModel;
import com.geeks18.virtualserver.drools.model.OrderRuleModel;
import com.geeks18.virtualserver.rule.dto.MainRuleDTO;
import com.geeks18.virtualserver.service.RuleServerService;

@Component
public class DroolsConfiguration {
 
    @Resource 
    RuleServerService ruleServerService;
    
    private KieContainer kieContainer;
    
    public KieContainer getKieContainer(){
    	if(kieContainer==null){
    		return setKieContainer();
    	}
    	return kieContainer;
    }
    public void refreshKieContainer(){
    	this.kieContainer=null;
    }
    private  KieContainer  setKieContainer() {
        KieServices kieServices = KieServices.Factory.get();
 
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        
        List<MainRuleDTO> list= ruleServerService.getAllRulesByTemplate("sample");
        list.forEach(x->{
        	System.out.println("----x>"+ x.getWhenRule());
        	System.out.println("----y>"+ x.getThenRule());
        	 kieFileSystem.write("src/main/resources/rule.drl",applyRuleTemplate(new OrderRuleModel(), x.getWhenRule(), x.getThenRule()));
        });
       
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
    
   
    
    static private String applyRuleTemplate(GenericRuleModel model, String when_rule,String then_rule)  {
        Map<String, Object> data = new HashMap<String, Object>();
        ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();

        data.put("when_rule", when_rule);
        data.put("then_rule", then_rule);
        data.put("objectType", model.getClass().getName());

        return objectDataCompiler.compile(Arrays.asList(data), Thread.currentThread().getContextClassLoader().getResourceAsStream("template.drt"));
    }
}