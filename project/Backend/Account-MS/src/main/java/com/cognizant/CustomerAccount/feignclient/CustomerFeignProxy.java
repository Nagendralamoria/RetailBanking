package com.cognizant.CustomerAccount.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cognizant.CustomerAccount.model.CustomerEntity;

@FeignClient(name = "customer", url = "${accountms.feign.url.customerms}")
public interface CustomerFeignProxy {

	@GetMapping("/getCustomerDetails/{id}")
	public CustomerEntity getCustomerDetails(@PathVariable(name = "id") String id);

}
