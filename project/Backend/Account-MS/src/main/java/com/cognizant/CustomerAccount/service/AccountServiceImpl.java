package com.cognizant.CustomerAccount.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.CustomerAccount.exceptionhandling.AccessDeniedException;
import com.cognizant.CustomerAccount.exceptionhandling.AccountNotFoundException;
import com.cognizant.CustomerAccount.feignclient.AuthFeignClient;
import com.cognizant.CustomerAccount.feignclient.TransactionFeign;
import com.cognizant.CustomerAccount.model.CustomerAccount;
import com.cognizant.CustomerAccount.model.CustomerAccountCreationStatus;
import com.cognizant.CustomerAccount.model.CustomerAccountInput;
import com.cognizant.CustomerAccount.model.AuthenticationResponse;
import com.cognizant.CustomerAccount.model.Statement;
import com.cognizant.CustomerAccount.repository.AccountRepository;
import com.cognizant.CustomerAccount.repository.StatementRepository;

@Service
public class AccountServiceImpl implements AccountService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@Autowired
	private AuthFeignClient authFeignClient;
	@Autowired
	private TransactionFeign transactionFeign;

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private StatementRepository statementRepository;

	@Override
	public CustomerAccountCreationStatus createAccount(String customerId, CustomerAccount customerAccount) {
		accountRepository.save(customerAccount);
		CustomerAccountCreationStatus customerAccountCreationStatus = new CustomerAccountCreationStatus(customerAccount.getAccountId(),
				"Sucessfully Created");
		LOGGER.info("CustomerAccount Created Successfully");
		return customerAccountCreationStatus;
	}

	
	@Override
	public List<CustomerAccount> getCustomerAccount(String token, String customerId) {
		List<CustomerAccount> accountList = accountRepository.findByCustomerId(customerId);
		for (CustomerAccount acc : accountList) {
			acc.setTransactions(transactionFeign.getTransactionsByAccId(token, acc.getAccountId()));
		}
		return accountList;
	}

	
	@Override
	public CustomerAccount getAccount(long accountId) {
		CustomerAccount customerAccount = accountRepository.findByAccountId(accountId);
		if (customerAccount == null) {
			throw new AccountNotFoundException("CustomerAccount Does Not Exist");
		}
		return customerAccount;
	}


	@Override
	public CustomerAccount updateBalance(CustomerAccountInput customerAccountInput) {
		LOGGER.info("CustomerAccount to update " + customerAccountInput.getAccountId());
		CustomerAccount toUpdateAcc = accountRepository.findByAccountId(customerAccountInput.getAccountId());
		toUpdateAcc.setCurrentBalance(toUpdateAcc.getCurrentBalance() - customerAccountInput.getAmount());
		return accountRepository.save(toUpdateAcc);
	}

	@Override
	public CustomerAccount updateDepositBalance(CustomerAccountInput customerAccountInput) {
		LOGGER.info("CustomerAccount to update " + customerAccountInput.getAccountId());
		CustomerAccount toUpdateAcc = accountRepository.findByAccountId(customerAccountInput.getAccountId());
		toUpdateAcc.setCurrentBalance(toUpdateAcc.getCurrentBalance() + customerAccountInput.getAmount());
		return accountRepository.save(toUpdateAcc);
	}

	
	@Override
	public AuthenticationResponse hasPermission(String token) {
		return authFeignClient.tokenValidation(token);
	}

	@Override
	public AuthenticationResponse hasEmployeePermission(String token) {
		AuthenticationResponse validity = authFeignClient.tokenValidation(token);
		if (!authFeignClient.getRole(validity.getUserid()).equalsIgnoreCase("EMPLOYEE")) {
			throw new AccessDeniedException("NOT ALLOWED");
		}
		return validity;
	}

	@Override
	public AuthenticationResponse hasCustomerPermission(String token) {
		AuthenticationResponse validity = authFeignClient.tokenValidation(token);
		if (!authFeignClient.getRole(validity.getUserid()).equalsIgnoreCase("CUSTOMER")) {
			throw new AccessDeniedException("NOT ALLOWED");
		}
		return validity;
	}

	
	@Override
	public List<CustomerAccount> getAllAccounts() {
		List<CustomerAccount> customerAccounts = accountRepository.findAll();
		return customerAccounts;
	}

		@Override
	public void deleteCustomer(String id) {
		List<CustomerAccount> list = new ArrayList<>();
		list = getAllAccounts();
		for (CustomerAccount customerAccount : list) {
			if (customerAccount.getCustomerId().equalsIgnoreCase(id)) {
				accountRepository.deleteById(customerAccount.getAccountId());
			}
		}

	}

	
	@Override
	public void updateStatement(CustomerAccountInput accInput, CustomerAccount newUpdateAccBal, String message) {
		long accountId = accInput.getAccountId();
		Statement statement = new Statement(accountId, accountId, accInput.getAmount(),
				newUpdateAccBal.getCurrentBalance(), newUpdateAccBal.getCurrentBalance(), new Date(), message);
		statementRepository.save(statement);
	}


	@Override
	public void updateStatement(CustomerAccount updatedSourceAccBal, CustomerAccount updatedTargetAccBal, double amount,
			String message) {
		Statement statement = new Statement(updatedSourceAccBal.getAccountId(), updatedTargetAccBal.getAccountId(),
				amount, updatedSourceAccBal.getCurrentBalance(), updatedTargetAccBal.getCurrentBalance(), new Date(),
				message);
		statementRepository.save(statement);

	}


	@Override
	public List<Statement> getAccountStatement(long accountId) {
		Date startDate = new Date();
		LocalDateTime date = LocalDateTime.now().minusDays(30);
		Date endDate = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
		List<Statement> statements = statementRepository.findStatementByAccountId(accountId, endDate, startDate);
		return statements;
	}

	@Override
	public List<Statement> getAccountStatement(long accountId, String from, String to) throws ParseException {
		Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
		Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);
		List<Statement> statements = statementRepository.findStatementByAccountId(accountId, fromDate, toDate);
		return statements;
	}

}