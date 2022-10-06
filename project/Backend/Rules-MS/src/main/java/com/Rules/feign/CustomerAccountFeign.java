package com.Rules.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.Rules.model.Account;
import com.Rules.model.AccountInput;

@FeignClient(name = "account-ms", url = "${feign.url-account-service}")
public interface CustomerAccountFeign {

	@PostMapping("/servicecharge")
	public ResponseEntity<Account> servicecharge(@RequestHeader("Authorization") String token,
			@RequestBody AccountInput accInput);

	@GetMapping("/find")
	public ResponseEntity<List<Account>> getAllacc(@RequestHeader("Authorization") String token);

}
