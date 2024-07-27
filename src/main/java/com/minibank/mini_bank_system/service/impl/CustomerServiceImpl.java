package com.minibank.mini_bank_system.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minibank.mini_bank_system.dto.AddressDTO;
import com.minibank.mini_bank_system.dto.CustomerDTO;
import com.minibank.mini_bank_system.entities.Account;
import com.minibank.mini_bank_system.entities.Customer;
import com.minibank.mini_bank_system.exception.ResourceNotFoundException;
import com.minibank.mini_bank_system.repository.AccountRepository;
import com.minibank.mini_bank_system.repository.CustomerRepository;
import com.minibank.mini_bank_system.service.CustomerService;
import com.minibank.mini_bank_system.service.mapper.AddressMapper;
import com.minibank.mini_bank_system.service.mapper.CustomerMapper;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static String CUSTOMER_NOT_TFOUND = "Customer not found";

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	@Transactional
	public CustomerDTO createCustomer(CustomerDTO customerDTO) {
		// Create or fetch accounts
		Set<Account> accounts = fetchOrCreateAccounts(customerDTO.getAccountIds());

		// Convert DTO to entity and handle addresses
		Customer customer = CustomerMapper.toCustomer(customerDTO, accounts);
		updateCustomerAddresses(customer, customerDTO.getAddresses());

		// Save and return the created customer
		customer = customerRepository.save(customer);
		return CustomerMapper.toCustomerDTO(customer);
	}

	private Set<Account> fetchOrCreateAccounts(Set<Long> accountIds) {
		if (accountIds == null || accountIds.isEmpty()) {
			return Set.of(createDefaultAccount());
		}
		return accountIds.stream().map(id -> accountRepository.findById(id).orElseGet(() -> createNewAccount(id)))
				.collect(Collectors.toSet());
	}

	private Account createDefaultAccount() {
		Account defaultAccount = new Account();
		defaultAccount.setAccountNumber(generateDefaultAccountNumber());
		defaultAccount.setNumberOfOwners(1);
		return accountRepository.save(defaultAccount);
	}

	private Account createNewAccount(Long id) {
		Account newAccount = new Account();
		newAccount.setAccountNumber("ACCT" + id);
		newAccount.setNumberOfOwners(1);
		return accountRepository.save(newAccount);
	}

	private String generateDefaultAccountNumber() {
		return "ACCT" + System.currentTimeMillis();
	}

	private void updateCustomerAddresses(Customer customer, List<AddressDTO> addressDTOs) {
		customer.getAddresses().clear(); // Remove old addresses
		addressDTOs.stream().filter(this::isAddressDTOValid).map(AddressMapper::toEntity)
				.peek(address -> address.setCustomer(customer)).forEach(customer::addAddress);
	}

	private boolean isAddressDTOValid(AddressDTO addressDTO) {
		return addressDTO.getStreet() != null && !addressDTO.getStreet().isEmpty() && addressDTO.getCity() != null
				&& !addressDTO.getCity().isEmpty() && addressDTO.getState() != null && !addressDTO.getState().isEmpty()
				&& addressDTO.getZipCode() != null && !addressDTO.getZipCode().isEmpty()
				&& addressDTO.getCountry() != null && !addressDTO.getCountry().isEmpty();
	}

	@Override
	@Transactional
	public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
		Customer existingCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_TFOUND + id));

		// Update customer details
		updateCustomerDetails(existingCustomer, customerDTO);

		// Save and return the updated customer
		existingCustomer = customerRepository.save(existingCustomer);
		return CustomerMapper.toCustomerDTO(existingCustomer);
	}

	private void updateCustomerDetails(Customer existingCustomer, CustomerDTO customerDTO) {
		existingCustomer.setName(customerDTO.getName());
		existingCustomer.setLastname(customerDTO.getLastname());
		existingCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
		existingCustomer.setEmail(customerDTO.getEmail());
		existingCustomer.setCustomerType(customerDTO.getCustomerType());
		updateCustomerAddresses(existingCustomer, customerDTO.getAddresses());
	}

	@Override
	@Transactional
	public List<CustomerDTO> searchCustomers(String searchTerm, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Customer> customerPage = customerRepository.findAllByNameContainingOrEmailContaining(searchTerm,
				searchTerm, pageable);
		return customerPage.getContent().stream().map(CustomerMapper::toCustomerDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDTO> getCustomerById(Long id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_TFOUND + id));

		// Ensure collection initialization
		Hibernate.initialize(customer.getAccounts());
		return Optional.of(CustomerMapper.toCustomerDTO(customer));
	}
}
