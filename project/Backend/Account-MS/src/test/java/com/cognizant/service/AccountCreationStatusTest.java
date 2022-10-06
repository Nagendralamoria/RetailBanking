package com.cognizant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.cognizant.CustomerAccount.model.CustomerAccountCreationStatus;

public class AccountCreationStatusTest {
	
	CustomerAccountCreationStatus ac=new CustomerAccountCreationStatus();
	CustomerAccountCreationStatus ac1=new CustomerAccountCreationStatus(3698,null);
	
	@Test 
	public void accIdTest()
	{
		ac.setAccountId(1234);
		assertEquals(1234, ac.getAccountId());
	}
	@Test
	public void messTest()
	{
		ac.setMessage(null);
		assertEquals(null, ac.getMessage());
	}
	
	@Test 
	public void accIdTest1()
	{
		assertEquals(3698, ac1.getAccountId());
	}
	@Test
	public void messTest1()
	{
		assertEquals(null, ac1.getMessage());
	}

}
