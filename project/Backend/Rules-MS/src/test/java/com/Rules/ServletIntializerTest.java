package com.Rules;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.Rules.RulesApplication;

@ExtendWith(MockitoExtension.class)
public class ServletIntializerTest {

	@Mock
	private SpringApplicationBuilder springApplicationBuilder;

	@Test
	public void main() {
		RulesApplication.main(new String[] {});
	}

}
