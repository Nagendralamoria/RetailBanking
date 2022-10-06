package com.cognizant.CustomerTransactions.controller;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.CustomerTransactions.repository.CustomerTransactionRepository;
import com.cognizant.CustomerTransactions.service.CustomerTransactionService;

@WebMvcTest
@ExtendWith(SpringExtension.class)

public class TransactionRestControllerTest {
	@SuppressWarnings("unused")
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CustomerTransactionRepository customerTransactionRepository;
	@Mock
	CustomerTransactionService customerTransactionService;
	
	
	@Test
	public void amountTest()
	{
		
	}

}
