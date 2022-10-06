package com.cognizant.CustomerAccount.controller;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.CustomerAccount.exceptionhandling.MinimumBalanceException;
import com.cognizant.CustomerAccount.feignclient.TransactionFeign;
import com.cognizant.CustomerAccount.model.CustomerAccount;
import com.cognizant.CustomerAccount.model.CustomerAccountCreationStatus;
import com.cognizant.CustomerAccount.model.CustomerAccountInput;
import com.cognizant.CustomerAccount.model.MessageDetails;
import com.cognizant.CustomerAccount.model.Statement;
import com.cognizant.CustomerAccount.model.Transaction;
import com.cognizant.CustomerAccount.model.TransactionInput;
import com.cognizant.CustomerAccount.service.AccountServiceImpl;

@RestController
@CrossOrigin()
public class CustomerAccountController { 

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAccountController.class);
	
	@Autowired
	private MessageDetails msd;
	@Autowired
	private AccountServiceImpl accountSI;
	@Autowired
	private TransactionFeign transactionFeign; 
	
	
	@GetMapping("/getAccount/{accountId}")
	public ResponseEntity<CustomerAccount> getAccount(@RequestHeader("Authorization") String token,@PathVariable long accountId) {
		accountSI.hasPermission(token);
		CustomerAccount accountReturnObject = accountSI.getAccount(accountId);
		LOGGER.info("CustomerAccount Details Returned Successfully");
		return new ResponseEntity<>(accountReturnObject, HttpStatus.OK);
	}

	@PostMapping("/createAccount/{customerId}")
	public ResponseEntity<?> createAccount(@RequestHeader("Authorization") String token,@PathVariable String customerId,@Valid @RequestBody CustomerAccount customerAccount) {
		accountSI.hasEmployeePermission(token);
		CustomerAccountCreationStatus returnObjAccountCreationStatus = accountSI.createAccount(customerId, customerAccount);
		if (returnObjAccountCreationStatus == null) {
			LOGGER.error("Customer Creation Unsuccessful");
			return new ResponseEntity<>("Customer Creation Unsuccessful", HttpStatus.NOT_ACCEPTABLE);
		}
			
		LOGGER.info("CustomerAccount Created Successfully");
		return new ResponseEntity<>(returnObjAccountCreationStatus, HttpStatus.CREATED);
	}
	

	@GetMapping("/getAccounts/{customerId}")
	public ResponseEntity<List<CustomerAccount>> getCustomerAccount(@RequestHeader("Authorization") String token,@PathVariable String customerId) {
		accountSI.hasPermission(token);
		LOGGER.info("CustomerAccount List Returned");
		return new ResponseEntity<>(accountSI.getCustomerAccount(token, customerId), HttpStatus.OK);
	}

	@PostMapping("/deposit")
	public ResponseEntity<CustomerAccount> deposit(@RequestHeader("Authorization") String token,@RequestBody CustomerAccountInput accInput) {
		accountSI.hasPermission(token);
		transactionFeign.makeDeposit(token, accInput);
		//Updating the new current balance after deposit
		CustomerAccount newUpdateAccBal = accountSI.updateDepositBalance(accInput);
		List<Transaction> list = transactionFeign.getTransactionsByAccId(token, accInput.getAccountId());
		newUpdateAccBal.setTransactions(list);
		accountSI.updateStatement(accInput,newUpdateAccBal,"Deposited");
		LOGGER.info("Amount Deposited");
		return new ResponseEntity<>(newUpdateAccBal, HttpStatus.OK);
	}


	@PostMapping("/withdraw")
	public ResponseEntity<CustomerAccount> withdraw(@RequestHeader("Authorization") String token, @RequestBody CustomerAccountInput accInput) {
		accountSI.hasPermission(token);
		try {
			transactionFeign.makeWithdraw(token, accInput);

		} catch (Exception e) {
			LOGGER.error("Minimum Balance 1000 should be maintaind");
			throw new MinimumBalanceException("Minimum Balance 1000 should be maintaind");
		}
		//Updating the new current balance after withdrawal
		CustomerAccount newUpdateAccBal = accountSI.updateBalance(accInput);
		List<Transaction> list = transactionFeign.getTransactionsByAccId(token, accInput.getAccountId());
		newUpdateAccBal.setTransactions(list);
		accountSI.updateStatement(accInput,newUpdateAccBal,"Withdrawn");
		LOGGER.info("Amount withdrawn successfully");
		return new ResponseEntity<>(newUpdateAccBal, HttpStatus.OK);
	}
	

	@PostMapping("/servicecharge")
	public ResponseEntity<CustomerAccount> servicecharge(@RequestHeader("Authorization") String token,@RequestBody CustomerAccountInput accInput) {
		accountSI.hasPermission(token);
		try {
			transactionFeign.makeServiceCharges(token, accInput);

		} catch (Exception e) {
			LOGGER.error("Minimum Balance 1000 should be maintaind");
			throw new MinimumBalanceException("Minimum Balance 1000 should be maintaind");
		}
		//Updating the new current balance after service charge deduction
		CustomerAccount newUpdateAccBal = accountSI.updateBalance(accInput);
		List<Transaction> list = transactionFeign.getTransactionsByAccId(token, accInput.getAccountId());
		newUpdateAccBal.setTransactions(list);
		accountSI.updateStatement(accInput,newUpdateAccBal,"Service charge");
		LOGGER.info("Service charge deducted successfully");
		return new ResponseEntity<>(newUpdateAccBal, HttpStatus.OK);
	}

	
	@PostMapping("/transaction")
	public ResponseEntity<?> transaction(@RequestHeader("Authorization") String token, @RequestBody TransactionInput transInput) {
		accountSI.hasPermission(token);
		boolean status = true;
		try {
			status = transactionFeign.makeTransfer(token, transInput);

		} catch (Exception e) {
			LOGGER.error("Minimum Balance 1000 should be maintaind");
			throw new MinimumBalanceException("Minimum Balance 1000 should be maintaind");
		}
		if (status == false) {
			return new ResponseEntity<>("Transaction Failed", HttpStatus.NOT_IMPLEMENTED);
		}
		
		CustomerAccount updatedSourceAccBal = accountSI.updateBalance(transInput.getSourceAccount());
		List<Transaction> sourceAcc = transactionFeign.getTransactionsByAccId(token,transInput.getSourceAccount().getAccountId());
		updatedSourceAccBal.setTransactions(sourceAcc);
		
		
		CustomerAccount updatedTargetAccBal = accountSI.updateDepositBalance(transInput.getTargetAccount());
		List<Transaction> targetAcc = transactionFeign.getTransactionsByAccId(token,transInput.getTargetAccount().getAccountId());
		updatedTargetAccBal.setTransactions(targetAcc);
		
	
		accountSI.updateStatement(updatedSourceAccBal,updatedTargetAccBal,transInput.getAmount(),"Transferred");
		LOGGER.info("Transaction completed successfully from CustomerAccount " + transInput.getSourceAccount().getAccountId()+ " to Target CustomerAccount " + transInput.getTargetAccount().getAccountId());
		msd.setMessage("Transaction Successfully Done..");
		return new ResponseEntity<>(msd,HttpStatus.OK);
	}

	
	@PostMapping("/checkBalance")
	public ResponseEntity<CustomerAccount> checkAccountBalance(@RequestHeader("Authorization") String token,@Valid @RequestBody CustomerAccountInput customerAccountInput) {
		accountSI.hasPermission(token);
		CustomerAccount customerAccount = accountSI.getAccount(customerAccountInput.getAccountId());
		return new ResponseEntity<>(customerAccount, HttpStatus.OK);
	}
	
	
	@GetMapping("/find")
	public ResponseEntity<List<CustomerAccount>> getAllAccount(@RequestHeader("Authorization") String token) {
		accountSI.hasPermission(token);
		List<CustomerAccount> customerAccount = accountSI.getAllAccounts();
		return new ResponseEntity<>(customerAccount, HttpStatus.OK);
	}
	
	
	@DeleteMapping("deleteCustomer/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> deleteCustomer(@RequestHeader("Authorization") String token, @PathVariable String id) {

		System.out.println("Starting deletion of account " + id);
		accountSI.deleteCustomer(id);
		System.out.println("Deleted");
		return new ResponseEntity<>("CustomerAccount Deleted successfully", HttpStatus.OK);
	}
	

	@GetMapping("/getAccountStatement/{accountId}")
	public ResponseEntity<List<Statement>> getAccountStatement(@RequestHeader("Authorization") String token,@PathVariable long accountId) {
		accountSI.hasPermission(token);
		List<Statement> statements = accountSI.getAccountStatement(accountId);
		LOGGER.info("CustomerAccount Statement Returned Successfully");
		return new ResponseEntity<>(statements, HttpStatus.OK);
	}
		
	
	@GetMapping("/getAccountStatement/{accountId}/{from}/{to}")
	public ResponseEntity<List<Statement>> getAccountStatement(@RequestHeader("Authorization") String token,@PathVariable long accountId,@PathVariable String from, @PathVariable String to) throws ParseException {
		accountSI.hasPermission(token);
		List<Statement> statements = accountSI.getAccountStatement(accountId,from,to);
		LOGGER.info("CustomerAccount Statement from "+from+" to "+to+" Returned Successfully");
		return new ResponseEntity<>(statements, HttpStatus.OK);
	}
}
