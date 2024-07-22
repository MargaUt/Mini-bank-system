package com.minibank.mini_bank_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minibank.mini_bank_system.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
    boolean existsByNameAndLastnameAndPhoneNumberAndEmail(String name, String lastname, String phoneNumber, String email);

}