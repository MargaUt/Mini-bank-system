package com.minibank.mini_bank_system.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minibank.mini_bank_system.dto.AddressDTO;
import com.minibank.mini_bank_system.dto.CustomerDTO;
import com.minibank.mini_bank_system.entities.Account;
import com.minibank.mini_bank_system.entities.Address;
import com.minibank.mini_bank_system.entities.Customer;
import com.minibank.mini_bank_system.exception.ResourceNotFoundException;
import com.minibank.mini_bank_system.repository.AccountRepository;
import com.minibank.mini_bank_system.repository.CustomerRepository;
import com.minibank.mini_bank_system.service.CustomerService;
import com.minibank.mini_bank_system.service.mapper.AddressMapper;
import com.minibank.mini_bank_system.service.mapper.CustomerMapper;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	@Transactional
	public CustomerDTO createCustomer(CustomerDTO customerDTO) {
		// Fetch or create accounts based on the account IDs before calling the mapper
		Set<Account> accounts = createOrFetchAccounts(customerDTO.getAccountIds());

		// Convert CustomerDTO to Customer entity, including the accounts
		Customer customer = CustomerMapper.toCustomer(customerDTO, accounts);

		// Handle addresses
		handleAddresses(customer, customerDTO.getAddresses());

		// Save the customer
		customer = customerRepository.save(customer);

		// Convert the saved Customer entity back to DTO
		return CustomerMapper.toCustomerDTO(customer);
	}

	private void handleAddresses(Customer customer, List<AddressDTO> addressDTOs) {
		for (AddressDTO addressDTO : addressDTOs) {
			Address address = AddressMapper.toEntity(addressDTO);
			address.setCustomer(customer);
			customer.addAddress(address);
		}
	}

	private Set<Account> createOrFetchAccounts(Set<Long> accountIds) {
		return accountIds.stream().map(id -> {
			Optional<Account> accountOpt = accountRepository.findById(id);
			if (accountOpt.isPresent()) {
				return accountOpt.get();
			} else {
				// Create a new account if it doesn't exist
				Account newAccount = new Account();
				newAccount.setAccountNumber("ACCT" + id);
				newAccount.setNumberOfOwners(1);
				return accountRepository.save(newAccount);
			}
		}).collect(Collectors.toSet());
	}

	@Override
	@Transactional
	public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
		Optional<Customer> existingCustomerOpt = customerRepository.findById(id);
		if (existingCustomerOpt.isPresent()) {
			Customer existingCustomer = existingCustomerOpt.get();
			updateCustomerDetails(existingCustomer, customerDTO);
			existingCustomer = customerRepository.save(existingCustomer);
			return CustomerMapper.toCustomerDTO(existingCustomer);
		} else {
			throw new ResourceNotFoundException("Customer not found with ID: " + id);
		}
	}

	// TODO enable updating with customer with addresses
	private void updateCustomerDetails(Customer existingCustomer, CustomerDTO customerDTO) {
		// Update fields
		existingCustomer.setName(customerDTO.getName());
		existingCustomer.setLastname(customerDTO.getLastname());
		existingCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
		existingCustomer.setEmail(customerDTO.getEmail());
		existingCustomer.setCustomerType(customerDTO.getCustomerType());

		// Clear existing addresses
		existingCustomer.getAddresses().clear();

		// Add new addresses
		for (AddressDTO addressDTO : customerDTO.getAddresses()) {
			Address address = AddressMapper.toEntity(addressDTO);
			address.setCustomer(existingCustomer);
			existingCustomer.addAddress(address);
		}
	}

	@Override
	@Transactional
	public List<CustomerDTO> searchCustomers(String searchTerm, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Customer> customerPage = customerRepository.findAllByNameContainingOrEmailContaining(searchTerm,
				searchTerm, pageable);
		return customerPage.getContent().stream().map(CustomerMapper::toCustomerDTO).collect(Collectors.toList());
	}

	// TODO fix that account would be displayed
	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDTO> getCustomerById(Long id) {
		return customerRepository.findById(id).map(CustomerMapper::toCustomerDTO);
	}

}