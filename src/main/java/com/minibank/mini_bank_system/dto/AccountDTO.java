package com.minibank.mini_bank_system.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.minibank.mini_bank_system.entities.Account;
import com.minibank.mini_bank_system.entities.Customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDTO {
	private Long id;
	private String accountNumber;
	private double balance;
	private Set<Long> ownerIds;

	public static AccountDTO fromEntity(Account account) {
		return AccountDTO.builder().id(account.getId()).accountNumber(account.getAccountNumber())
				.balance(account.getBalance())
				.ownerIds(account.getOwners().stream().map(Customer::getId).collect(Collectors.toSet())).build();
	}

	public static Account toEntity(AccountDTO accountDTO) {
		Account account = new Account();
		account.setId(accountDTO.getId());
		account.setAccountNumber(accountDTO.getAccountNumber());
		account.setBalance(accountDTO.getBalance());
		return account;
	}
}
