package com.minibank.mini_bank_system.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.minibank.mini_bank_system.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	boolean existsByNameAndLastnameAndPhoneNumberAndEmail(String name, String lastname, String phoneNumber,
			String email);

	Page<Customer> findAllByNameContainingOrEmailContaining(String name, String email, Pageable pageable);

	@EntityGraph(attributePaths = { "addresses" })
	Optional<Customer> findById(Long id);

}