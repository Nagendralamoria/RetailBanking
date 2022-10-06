package com.cognizant.CustomerAccount.model;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountCreationStatus {


	@Id
	@Getter
	@Setter
	private long accountId;
	@Getter
	@Setter
	private String message;


}
