package com.cognizant.CustomerTransactions.service;

import com.cognizant.CustomerTransactions.models.AccountInput1;
import com.cognizant.CustomerTransactions.models.TransactionInput;

public interface CustomerTransactionServiceInterface {

	public boolean makeTransfer(String token, TransactionInput transactionInput);

	public boolean makeWithdraw(String token, AccountInput1 accountInput1);

	public boolean makeDeposit(String token, AccountInput1 accountInput1);

	public boolean makeServiceCharges(String token, AccountInput1 accountInput1);
}
