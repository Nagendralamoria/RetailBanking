package com.cognizant.CustomerAccount.service;

import java.text.ParseException;
import java.util.List;

import com.cognizant.CustomerAccount.model.CustomerAccount;
import com.cognizant.CustomerAccount.model.CustomerAccountCreationStatus;
import com.cognizant.CustomerAccount.model.CustomerAccountInput;
import com.cognizant.CustomerAccount.model.AuthenticationResponse;
import com.cognizant.CustomerAccount.model.Statement;

public interface AccountService {

	public CustomerAccountCreationStatus createAccount(String customerId, CustomerAccount customerAccount);

	public List<CustomerAccount> getCustomerAccount(String token, String customerId);

	public CustomerAccount getAccount(long accountId);

	public AuthenticationResponse hasPermission(String token);

	public AuthenticationResponse hasEmployeePermission(String token);

	public AuthenticationResponse hasCustomerPermission(String token);

	public CustomerAccount updateDepositBalance(CustomerAccountInput customerAccountInput);

	public CustomerAccount updateBalance(CustomerAccountInput customerAccountInput);

	public List<CustomerAccount> getAllAccounts();

	List<Statement> getAccountStatement(long accountId);

	List<Statement> getAccountStatement(long accountId, String from, String to) throws ParseException;

	void updateStatement(CustomerAccount updatedSourceAccBal, CustomerAccount updatedTargetAccBal, double amount, String message);

	void updateStatement(CustomerAccountInput accInput, CustomerAccount newUpdateAccBal, String message);

	void deleteCustomer(String id);

}
