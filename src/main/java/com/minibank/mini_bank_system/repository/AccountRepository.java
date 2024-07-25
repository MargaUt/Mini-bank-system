package com.minibank.mini_bank_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minibank.mini_bank_system.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
