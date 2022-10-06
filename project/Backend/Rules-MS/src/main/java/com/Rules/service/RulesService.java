package com.Rules.service;

import com.Rules.model.Account;
import com.Rules.model.AuthenticationResponse;
import com.Rules.model.RulesInput;

public interface RulesService {

	public boolean evaluate(RulesInput account);

	public AuthenticationResponse hasPermission(String token);

	public double serviceCharges(Account account);

}
