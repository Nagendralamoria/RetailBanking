package com.Rules.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.Rules.exception.MinimumBalanceException;
import com.Rules.feign.CustomerAccountFeign;
import com.Rules.model.Account;
import com.Rules.model.AccountInput;
import com.Rules.model.RulesInput;
import com.Rules.service.RulesService;

@RestController
@CrossOrigin()
public class RulesController {

	private static final String INVALID = "Send Valid Details.";
	@Autowired
	public RulesService rs;
	@Autowired
	CustomerAccountFeign customerAccountFeign;

	@PostMapping("/evaluateMinBal")
	public ResponseEntity<?> evaluate(@RequestBody RulesInput account) throws MinimumBalanceException {
		if (account.getCurrentBalance() == 0) {
			throw new MinimumBalanceException(INVALID);
		} else {
			boolean status = rs.evaluate(account);

			return new ResponseEntity<Boolean>(status, HttpStatus.OK);
		}
	}

	@PostMapping("/serviceCharges")
	public ResponseEntity<?> serviceCharges(@RequestHeader("Authorization") String token) {
		rs.hasPermission(token);
		try {
			List<Account> body = customerAccountFeign.getAllacc(token).getBody();
			for (Account acc : body) {
				if (rs.serviceCharges(acc) > 0) {
					customerAccountFeign.servicecharge(token,
							new AccountInput(acc.getAccountId(), rs.serviceCharges(acc)));
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return ResponseEntity.ok(customerAccountFeign.getAllacc(token).getBody());
	}

}
