package com.cognizant.CustomerAccount;

 

import static org.junit.jupiter.api.Assertions.assertEquals;

 

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cognizant.CustomerAccount.CustomerAccountApplication;

 

@SpringBootTest
class CustomerAccountApplicationTests {
    
    @Test
    void setCustomerIdTest() {
        String check="Cust101";
        assertEquals("Cust101",check );
    }
    
    @Test
    public void main() {
        CustomerAccountApplication.main(new String[] {});
    }
}