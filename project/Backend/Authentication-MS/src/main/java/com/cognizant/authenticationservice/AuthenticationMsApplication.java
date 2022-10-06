package com.cognizant.authenticationservice;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class AuthenticationMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationMsApplication.class, args);
	}
	@Bean
	public Docket swaggerConfiguration() {

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.cognizant.authenticationservice.controller")).build().apiInfo(apiInfo());
 
	}

	
	private ApiInfo apiInfo() {
        return new ApiInfo("Authorization Api", "Sample API for Authorization", "1.0", "path/to/terms",
                new Contact("RetailBanking", "http://www.google.com", "Retailbanking@gmail.com"), "API License",
                "http://www.google.com", Collections.emptyList());
    }

}
