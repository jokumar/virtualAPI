package com.geeks18.virtualserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan("com.geeks18.*")
@SpringBootApplication
public class VirtualServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualServerApplication.class, args);
	}

}
