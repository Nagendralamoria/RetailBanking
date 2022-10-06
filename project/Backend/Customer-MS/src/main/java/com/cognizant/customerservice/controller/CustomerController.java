package com.cognizant.customerservice.controller;

import java.net.BindException;
import java.time.DateTimeException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.customerservice.feign.AuthorizationFeign;
import com.cognizant.customerservice.model.CustomerEntity;
import com.cognizant.customerservice.model.MessageDetails;
import com.cognizant.customerservice.service.CustomerService;

@RestController
@CrossOrigin()
public class CustomerController {

	@Autowired
	private CustomerService cs;
	
	@Autowired
	private MessageDetails msd;
	
	@Autowired
	AuthorizationFeign authorizationFeign;

	@PostMapping("/createCustomer")
	public ResponseEntity<?> createCustomer(@RequestHeader("Authorization") String token,
			@Valid @RequestBody CustomerEntity customer, BindingResult bindingResult)
			throws DateTimeException, BindException {
		if (bindingResult.hasErrors()) {
			throw new BindException();
		}
		cs.hasEmployeePermission(token);
		CustomerEntity customerEntity = cs.createCustomer(token, customer);
		if (customerEntity != null)
			return new ResponseEntity<>(customerEntity, HttpStatus.CREATED);
		else
			return new ResponseEntity<>("Customer Creation is UNSUCCESSFUL", HttpStatus.NOT_ACCEPTABLE);
	}



	@PostMapping("/updateCustomer")
	public CustomerEntity updateCustomer(@RequestHeader("Authorization") String token,
			@Valid @RequestBody CustomerEntity customer) {
		cs.hasEmployeePermission(token);
		return cs.updateCustomer(token, customer);
	}

	@GetMapping("/getCustomerDetails/{id}")
	public ResponseEntity<?> getCustomerDetails(@RequestHeader("Authorization") String token, @PathVariable String id) {
		cs.hasPermission(token);
		CustomerEntity toReturnCustomerDetails = cs.getCustomerDetail(token, id);
		if (toReturnCustomerDetails == null)
			return new ResponseEntity<>("Customer Userid " + id + " DOES NOT EXISTS", HttpStatus.NOT_ACCEPTABLE);
		toReturnCustomerDetails.setPassword(null);
		return new ResponseEntity<>(toReturnCustomerDetails, HttpStatus.OK);
	}


	@DeleteMapping("/deleteCustomer/{id}")
	public ResponseEntity<?> deleteCustomer(@RequestHeader("Authorization") String token, @PathVariable String id) {
		cs.hasPermission(token);
		CustomerEntity toReturnCustomerDetails = cs.getCustomerDetail(token, id);
		if (toReturnCustomerDetails == null)
			return new ResponseEntity<>("Customer Userid " + id + " DOES NOT EXISTS", HttpStatus.NOT_ACCEPTABLE);
		toReturnCustomerDetails.setPassword(null);

		boolean deleteCustomer = cs.deleteCustomer(id);
		
		if(deleteCustomer) {
			
			msd.setMessage("CUSTOMER DELETED");
			
			return new ResponseEntity<>(msd, HttpStatus.OK);			
		}
		return new ResponseEntity<>("Customer Userid " + id + " DOES NOT EXISTS", HttpStatus.NOT_ACCEPTABLE);
	}

	@GetMapping("/check")
    public String checkAccessWWithoutValidation(@RequestHeader("Authorization") String token) {
        cs.hasEmployeePermission(token);
        return "Your Token is valid";
    }
	@PostMapping("/saveCustomer")
    public CustomerEntity saveCustomer(@RequestHeader("Authorization") String token,
            @Valid @RequestBody CustomerEntity customer) {
        cs.hasEmployeePermission(token);
        CustomerEntity customerEntity = cs.saveCustomer(token, customer);
        if (customerEntity != null)
            return customerEntity;
        else
            return null;
    }


}
