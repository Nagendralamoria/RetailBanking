package com.Rules.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.Rules.controller.RulesController;
import com.Rules.exception.MinimumBalanceException;
import com.Rules.feign.CustomerAccountFeign;
import com.Rules.feign.AuthorizationFeign;
import com.Rules.model.Account;
import com.Rules.model.AuthenticationResponse;
import com.Rules.model.RulesInput;
import com.Rules.model.ServiceResponse;
import com.Rules.service.RulesServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RulesController.class)
@ImportAutoConfiguration({ RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class,
		FeignAutoConfiguration.class })
class RulesControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	AuthorizationFeign authProxy;

	@MockBean
	RulesServiceImpl rulesService;

	@Mock
	CustomerAccountFeign customerAccountFeign;

	@Test
	void evaluateTest() throws Exception {
		when(rulesService.hasPermission("token")).thenReturn(new AuthenticationResponse("Employee101", "emp", true));
		RulesInput inp = new RulesInput(101, 1200, 100);
		when(rulesService.evaluate(inp)).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.post("/evaluateMinBal").content(asJsonString(inp))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "token")).andExpect(status().isOk());

	}

	@Test
	void evaluateTestEqual() throws Exception {
		when(rulesService.hasPermission("token")).thenReturn(new AuthenticationResponse("Employee101", "emp", true));
		RulesInput inp = new RulesInput(101, 100, 100);
		when(rulesService.evaluate(inp)).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.post("/evaluateMinBal").content(asJsonString(inp))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "token")).andExpect(status().isOk());

	}

	@Test
	void evaluateTestNeg() throws Exception {
		when(rulesService.hasPermission("token")).thenReturn(new AuthenticationResponse("Employee101", "emp", true));
		RulesInput inp = new RulesInput(101, 200, 100);
		when(rulesService.evaluate(inp)).thenReturn(false);
		mockMvc.perform(MockMvcRequestBuilders.post("/evaluateMinBal").content(asJsonString(inp))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "token")).andExpect(status().isOk());

	}

	@Test
	void serviceChargesTestNegative() throws Exception {
		when(rulesService.hasPermission("token")).thenReturn(new AuthenticationResponse("Employee101", "emp", true));
		when(customerAccountFeign.getAllacc("token"))
				.thenReturn(new ResponseEntity<List<Account>>(new ArrayList<>(), HttpStatus.OK));
		mockMvc.perform(MockMvcRequestBuilders.post("/serviceCharges").header("Authorization", "token"))
				.andExpect(status().is(500));
		verify(rulesService, timeout(1)).hasPermission("token");

	}

	@Test
	public void MinimumBal() throws MinimumBalanceException, Exception {
		RulesController con = new RulesController();
		RulesInput account = new RulesInput(0, 0, 0);
		Throwable exception = assertThrows(MinimumBalanceException.class, () -> con.evaluate(account));
		assertEquals("Send Valid Details.", exception.getMessage());

	}

	public static String asJsonString(final Object obj) throws JsonProcessingException {

		final ObjectMapper mapper = new ObjectMapper();
		final String jsonContent = mapper.writeValueAsString(obj);
		return jsonContent;

	}

}