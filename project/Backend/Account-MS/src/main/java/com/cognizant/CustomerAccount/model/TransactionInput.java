package com.cognizant.CustomerAccount.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInput {
	
	private CustomerAccountInput sourceAccount;
	private CustomerAccountInput targetAccount;
	@Positive(message = "Amount should be positive")
	@Min(value = 1, message = "Amount should be greater than 1")
	private double amount;
	private String reference;

}