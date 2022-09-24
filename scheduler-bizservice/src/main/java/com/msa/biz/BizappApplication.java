package com.msa.biz;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;


@Configuration
@ComponentScan(basePackages = {"com.msa", "biz"})
@RestController
@SpringBootApplication
public class BizappApplication {

	public static void main(String[] args) {
		SpringApplication.run(BizappApplication.class, args);
	}
	
	@Value("${spring.application.name}")
	private String appName;


}
