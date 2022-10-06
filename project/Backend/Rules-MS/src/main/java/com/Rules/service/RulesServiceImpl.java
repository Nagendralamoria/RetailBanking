package com.Rules.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rules.exception.AccessDeniedException;
import com.Rules.feign.AuthorizationFeign;
import com.Rules.model.Account;
import com.Rules.model.AuthenticationResponse;
import com.Rules.model.RulesInput;

@Service
public class RulesServiceImpl implements RulesService {

	@Autowired
	AuthorizationFeign authorizationFeign;

	@Override
	public boolean evaluate(RulesInput account) {
		int min = 1000;
		double check = account.getCurrentBalance() - account.getAmount();
		if (check >= min)
			return true;
		else
			return false;
	}

	@Override
	public AuthenticationResponse hasPermission(String token) {
		AuthenticationResponse validity = authorizationFeign.getValidity(token);
		if (!authorizationFeign.getRole(validity.getUserid()).equals("EMPLOYEE"))
			throw new AccessDeniedException("NOT ALLOWED");
		else
			return validity;
	}

	@Override
	public double serviceCharges(Account account) {
		double detected = account.getCurrentBalance() / 10;
		if (account.getCurrentBalance() < 2000 && (account.getCurrentBalance() - detected) > 0) {
			return detected;
		} 
		return 0.0;
	}

}
