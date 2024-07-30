package com.minibank.mini_bank_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.minibank.mini_bank_system.entities.Account;
import com.minibank.mini_bank_system.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	boolean existsByNameAndLastnameAndPhoneNumberAndEmail(String name, String lastname, String phoneNumber,
			String email);

	Page<Customer> findAllByNameContainingOrLastnameContainingOrEmailContaining(String name, String lastname,
			String email, Pageable pageable);

	@EntityGraph(attributePaths = { "addresses" })
	Optional<Customer> findById(Long id);

	List<Customer> findByAccount(Account account);

}