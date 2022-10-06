package com.cognizant.CustomerAccount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.cognizant.CustomerAccount.exceptionhandling.MinimumBalanceException;

public class MinimumBalanceTest {
	
	@Test
	public void MinimumException() {
		
		MinimumBalanceException e1=new  MinimumBalanceException("hi");
		MinimumBalanceException e2=new  MinimumBalanceException("hi");
		assertThat(e1).isNotEqualTo(e2);
		
	}
	
	@Test
	public void MinimumExceptionNull() {
		
		MinimumBalanceException e1=new  MinimumBalanceException();
		MinimumBalanceException e2=new  MinimumBalanceException();
		assertThat(e1).isNotEqualTo(e2);
		
	}

}
