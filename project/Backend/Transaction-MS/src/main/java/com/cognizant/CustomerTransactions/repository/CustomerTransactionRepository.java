package com.cognizant.CustomerTransactions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.CustomerTransactions.models.Transaction;

public interface CustomerTransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findBySourceAccountIdOrTargetAccountIdOrderByInitiationDate(long id, long id2);

	List<Transaction> findByTargetAccountIdOrderByInitiationDate(long accId);

	List<Transaction> findBySourceAccountIdOrderByInitiationDate(int i);
}
