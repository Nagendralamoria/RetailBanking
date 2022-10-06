package com.cognizant.CustomerAccount.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cognizant.CustomerAccount.model.AuthenticationResponse;


@FeignClient(name = "auth-ms", url = "${accountms.feign.url.auththenticationms}")
public interface AuthFeignClient {


	@GetMapping("/validateToken")
	public AuthenticationResponse tokenValidation(@RequestHeader("Authorization") String token);

	@GetMapping("/role/{id}")
	public String getRole(@PathVariable("id") String id);

}