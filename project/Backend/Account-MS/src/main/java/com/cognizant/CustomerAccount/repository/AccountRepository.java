package com.cognizant.CustomerAccount.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cognizant.CustomerAccount.model.CustomerAccount;


@Repository
public interface AccountRepository extends JpaRepository<CustomerAccount, Long> {

	@Query(nativeQuery = true, value = "SELECT * from ACCOUNT a WHERE a.account_Id = :accountId")
	CustomerAccount findByAccountId(@Param(value = "accountId") long accountId);

	List<CustomerAccount> findByCustomerId(String customerId);

}
