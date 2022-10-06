package com.cognizant.CustomerAccount;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.cognizant.CustomerAccount.controller.CustomerAccountController;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableFeignClients
@EnableSwagger2
public class CustomerAccountApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAccountController.class);

	public static void main(String[] args) { 
		SpringApplication.run(CustomerAccountApplication.class, args);
		LOGGER.info("CustomerAccount microservice started....");
	}
	

	
	@Bean
	public Docket swaggerConfiguration() {

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.cognizant.CustomerAccount.controller")).build().apiInfo(apiInfo());
 
	}

	
	private ApiInfo apiInfo() {
		return new ApiInfo("CustomerAccount Management Service", "Retail Banking Project", "API", "Terms of service",
				new Contact("Peoples' Bank", "", "abc@email.com"), "License of API", "", Collections.emptyList());
	}

}
