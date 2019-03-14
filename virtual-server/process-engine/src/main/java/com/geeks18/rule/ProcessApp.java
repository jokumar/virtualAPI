package com.geeks18.rule;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

	@Configuration
	@SpringBootApplication
	public class ProcessApp {

		public static void main(String[] args) {
			SpringApplication.run(ProcessApp.class, args);
		}
		@Bean
		public KieSession kieSession(){
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			//Get the session named kseesion-rule that we defined in kmodule.xml above.
			//Also by default the session returned is always stateful. 
			return  kContainer.newKieSession("ksession-rule");
		}
	}

