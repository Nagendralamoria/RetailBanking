package com.cognizant.CustomerAccount.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccountErrorResponse {

	@Getter
	@Setter
	private LocalDateTime timestamp;
	@Getter
	@Setter
	private HttpStatus status;
	@Getter
	@Setter
	private String reason;
	@Getter
	@Setter
	private String message;
}
