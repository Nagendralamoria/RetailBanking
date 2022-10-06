package com.cognizant.CustomerTransactions.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.CustomerTransactions.exception.MinimumBalanceException;
import com.cognizant.CustomerTransactions.feign.CustomerAccountFeign;
import com.cognizant.CustomerTransactions.feign.RulesFeign;
import com.cognizant.CustomerTransactions.models.Account;
import com.cognizant.CustomerTransactions.models.AccountInput1;
import com.cognizant.CustomerTransactions.models.RulesInput;
import com.cognizant.CustomerTransactions.models.Transaction;
import com.cognizant.CustomerTransactions.models.TransactionInput;
import com.cognizant.CustomerTransactions.repository.CustomerTransactionRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class CustomerTransactionService implements CustomerTransactionServiceInterface {

	@Autowired
	private CustomerAccountFeign customerAccountFeign;

	@Autowired
	private CustomerTransactionRepository customerTransactionRepository;

	@Autowired
	private RulesFeign ruleFeign;

	
	@Override
	public boolean makeTransfer(String token, TransactionInput transactionInput) throws MinimumBalanceException {

		Account sourceAccount = null;
		Account targetAccount = null;

		long sourceAccountNumber = transactionInput.getSourceAccount().getAccountId();
		sourceAccount = customerAccountFeign.getAccount(token, sourceAccountNumber);
			Boolean check =  (Boolean) ruleFeign.evaluate(new RulesInput(sourceAccount.getAccountId(),
					sourceAccount.getCurrentBalance(), transactionInput.getAmount())).getBody();
			
			if (check.booleanValue() == false)
				throw new MinimumBalanceException("Minimum Balance 1000 should be maintaind");
		
		long targetAccountNumber = transactionInput.getTargetAccount().getAccountId();
		targetAccount = customerAccountFeign.getAccount(token, targetAccountNumber);

		if (sourceAccount != null && targetAccount != null) {
			if (isAmountAvailable(transactionInput.getAmount(), sourceAccount.getCurrentBalance())) {

				Transaction sourcetransaction = new Transaction();

				sourcetransaction.setAmount(transactionInput.getAmount());
				sourcetransaction.setSourceAccountId(sourceAccount.getAccountId());
				sourcetransaction.setSourceOwnerName(sourceAccount.getOwnerName());
				sourcetransaction.setTargetAccountId(targetAccount.getAccountId());
				sourcetransaction.setTargetOwnerName(targetAccount.getOwnerName());
				sourcetransaction.setInitiationDate(LocalDateTime.now());
				sourcetransaction.setReference("transfer");
				customerTransactionRepository.save(sourcetransaction);
				return true;
			}
		}
		return false;
	}
		

	
	
	private boolean isAmountAvailable(double amount, double accountBalance) {
		log.info("method to check wether the amount is available");
		return (accountBalance - amount) > 0;
	}

	
	@SuppressWarnings("unused")
	@Override
	public boolean makeWithdraw(String token, AccountInput1 accountInput1) {
		log.info("method to make a withdraw");
		Account sourceAccount = null;

		long accNumber = accountInput1.getAccountId();
		sourceAccount = customerAccountFeign.getAccount(token, accNumber);
		
			Boolean check = (Boolean) ruleFeign.evaluate(new RulesInput(accountInput1.getAccountId(),
					sourceAccount.getCurrentBalance(), accountInput1.getAmount() ) ).getBody();
			
			if (!check.booleanValue())
				throw new MinimumBalanceException("Minimum Balance 1000 should be maintaind");
		
		if (sourceAccount != null) {
			Transaction transaction = new Transaction();
			transaction.setSourceAccountId(sourceAccount.getAccountId());
			transaction.setSourceOwnerName(sourceAccount.getOwnerName());
			transaction.setTargetAccountId(sourceAccount.getAccountId());
			transaction.setTargetOwnerName(sourceAccount.getOwnerName());
			transaction.setInitiationDate(LocalDateTime.now());
			transaction.setReference("withdrawl");
			transaction.setAmount(accountInput1.getAmount());
			customerTransactionRepository.save(transaction);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean makeServiceCharges(String token, AccountInput1 accountInput1) {
		log.info("method to make a service charges");
		Account sourceAccount = null;

		long accNumber = accountInput1.getAccountId();
		sourceAccount = customerAccountFeign.getAccount(token, accNumber);
		if (sourceAccount != null) {
			Transaction transaction = new Transaction();
			transaction.setSourceAccountId(sourceAccount.getAccountId());
			transaction.setSourceOwnerName(sourceAccount.getOwnerName());
			transaction.setTargetAccountId(sourceAccount.getAccountId());
			transaction.setTargetOwnerName(sourceAccount.getOwnerName());
			transaction.setInitiationDate(LocalDateTime.now());
			transaction.setReference("service charge");
			transaction.setAmount(accountInput1.getAmount());
			customerTransactionRepository.save(transaction);
			return true;
		}
		
		return false;
		
	}


	@Override
	public boolean makeDeposit(String token, AccountInput1 accountInput1) {
		log.info("method to make a deposit");
		Account sourceAccount = null;

		long accNumber = accountInput1.getAccountId();
		sourceAccount = customerAccountFeign.getAccount(token, accNumber);
		if (sourceAccount != null) {
			Transaction transaction = new Transaction();
			transaction.setSourceAccountId(sourceAccount.getAccountId());
			transaction.setSourceOwnerName(sourceAccount.getOwnerName());
			transaction.setTargetAccountId(sourceAccount.getAccountId());
			transaction.setTargetOwnerName(sourceAccount.getOwnerName());
			transaction.setInitiationDate(LocalDateTime.now());
			transaction.setReference("deposit");
			transaction.setAmount(accountInput1.getAmount());
			customerTransactionRepository.save(transaction);
			return true;
		}
		return false;
	}
}
