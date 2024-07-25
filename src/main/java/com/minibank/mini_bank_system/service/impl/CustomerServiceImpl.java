package com.minibank.mini_bank_system.service.impl;

import java.util.List;
import java.util.Objects;
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

@Service
public class CustomerServiceImpl implements CustomerService {

	// TODO:
	// Implement several tests for CustomerServiceImpl

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	@Transactional
	public CustomerDTO createCustomer(CustomerDTO customerDTO) {
		Customer customer = convertToEntity(customerDTO);

		// Handle addresses
		handleAddresses(customer, customerDTO.getAddresses());

		// Assign accounts to customer, creating them if necessary
		Set<Account> accountsToAdd = createOrFetchAccounts(customerDTO.getAccountIds());
		customer.setAccounts(accountsToAdd);

		customer = customerRepository.save(customer);
		return convertToDTO(customer);
	}

	private void handleAddresses(Customer customer, List<AddressDTO> addressDTOs) {
		for (AddressDTO addressDTO : addressDTOs) {
			Address address = convertToEntity(addressDTO);
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
				newAccount.setBalance(0.0);
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
			return convertToDTO(existingCustomer);
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
			Address address = convertToEntity(addressDTO);
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
		return customerPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// TODO fix that account would be displayed
	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDTO> getCustomerById(Long id) {
		return customerRepository.findById(id).map(this::convertToDTO);
	}

	// TODO move converted to Entities and DTOs
	private CustomerDTO convertToDTO(Customer customer) {
		return CustomerDTO.builder().id(customer.getId()).name(customer.getName()).lastname(customer.getLastname())
				.phoneNumber(customer.getPhoneNumber()).email(customer.getEmail())
				.customerType(customer.getCustomerType())
				.addresses(customer.getAddresses().stream().map(this::convertToDTO)
						.collect(Collectors.toList()))
				.accountIds(customer.getAccounts().stream().map(Account::getId)
						.collect(Collectors.toSet()))
				.build();
	}

	private AddressDTO convertToDTO(Address address) {
		AddressDTO dto = new AddressDTO();
		dto.setId(address.getId());
		dto.setStreet(address.getStreet());
		dto.setCity(address.getCity());
		dto.setState(address.getState());
		dto.setZipCode(address.getZipCode());
		dto.setCountry(address.getCountry());

		return dto;
	}

	private Set<Account> accountDTOsToEntities(Set<Long> accountIds) {
		return accountIds.stream().map(id -> {
			Optional<Account> accountOpt = accountRepository.findById(id);
			return accountOpt.orElse(null);
		}).filter(Objects::nonNull).collect(Collectors.toSet());
	}

	private Customer convertToEntity(CustomerDTO customerDTO) {
		// Fetch or create accounts based on the account IDs
		Set<Account> accounts = accountDTOsToEntities(customerDTO.getAccountIds());

		// Convert CustomerDTO to Customer entity
		return Customer.builder().id(customerDTO.getId()).name(customerDTO.getName())
				.lastname(customerDTO.getLastname()).phoneNumber(customerDTO.getPhoneNumber())
				.email(customerDTO.getEmail()).customerType(customerDTO.getCustomerType())
				.addresses(customerDTO.getAddresses().stream().map(this::convertToEntity).collect(Collectors.toList()))
				.accounts(accounts).build();
	}

	private Address convertToEntity(AddressDTO addressDTO) {
		return Address.builder().street(addressDTO.getStreet()).city(addressDTO.getCity()).state(addressDTO.getState())
				.zipCode(addressDTO.getZipCode()).country(addressDTO.getCountry()).build();
	}

}