package com.Rules.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.Rules.model.ErrorDetails;

class ErrorDetailsTest {

	ErrorDetails det = new ErrorDetails();

	@Test
	void setUserIdTest() {
		det.setDetails("/notresponding");
		assertEquals("/notresponding", det.getDetails());
	}

	@Test
	void setNameTest() {
		det.setMessage("bhavya");
		assertEquals("bhavya", det.getMessage());
	}
}
