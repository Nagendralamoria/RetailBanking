package com.cognizant.CustomerAccount.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountInput {
	
	
	@Getter
	@Setter
	@NotNull(message = "Account Number is required")
	private long accountId;
	@Getter
	@Setter
	@NotNull(message = "Amount cannot be NULL")
	private double amount;

}