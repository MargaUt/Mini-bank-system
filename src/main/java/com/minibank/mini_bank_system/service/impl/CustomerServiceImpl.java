package com.minibank.mini_bank_system.service.impl;

import java.util.List;
import java.util.Optional;
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
import com.minibank.mini_bank_system.exception.CustomerAlreadyExistsException;
import com.minibank.mini_bank_system.exception.ResourceNotFoundException;
import com.minibank.mini_bank_system.repository.AccountRepository;
import com.minibank.mini_bank_system.repository.CustomerRepository;
import com.minibank.mini_bank_system.service.CustomerService;
import com.minibank.mini_bank_system.service.mapper.AddressMapper;
import com.minibank.mini_bank_system.service.mapper.CustomerMapper;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final String CUSTOMER_NOT_FOUND = "Customer with  not found with this ";
	private static final String CUSTOMER_COULD_NOT_BE_ASSIGNED = "Cannot add customer: Another customer with the same name, lastname, or email is already assigned to this account.";

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	@Transactional
	public CustomerDTO createCustomer(CustomerDTO customerDTO) {
		// Fetch or create an account
		Account account = fetchOrCreateAccount(customerDTO.getAccountNumber());

		// Check if the customer already exists in the account (in-memory check)
		if (isCustomerDuplicate(customerDTO, account)) {
			throw new CustomerAlreadyExistsException(CUSTOMER_COULD_NOT_BE_ASSIGNED);
		}

		// Convert DTO to entity and handle addresses
		Customer customer = CustomerMapper.toCustomer(customerDTO, account);
		updateCustomerAddresses(customer, customerDTO.getAddresses());

		// Save the customer
		customer = customerRepository.save(customer);

		// Update the account with the new customer
		updateAccountWithCustomer(account, customer);

		// Fetch the number of customers associated with the account
		int numberOfAssignedCustomers = account.getOwners().size();

		// Create a DTO with the number of assigned customers
		CustomerDTO customerDTOResponse = CustomerMapper.toCustomerDTO(customer);
		customerDTOResponse.setNumberOfAssignedCustomers(numberOfAssignedCustomers);

		return customerDTOResponse;
	}

	@Override
	@Transactional
	public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
		Customer existingCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND, id));

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
	@Transactional(readOnly = true)
	public List<CustomerDTO> searchCustomers(String searchTerm, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		// Fetch customers based on the search term
		Page<Customer> customerPage = customerRepository.findAllByNameContainingOrLastnameContainingOrEmailContaining(
				searchTerm, searchTerm, searchTerm, pageable);

		List<CustomerDTO> customerDTOList = customerPage.getContent().stream().map(customer -> {
			CustomerDTO customerDTO = CustomerMapper.toCustomerDTO(customer);
			if (customer.getAccount() != null) {
				Account account = customer.getAccount();
				int numberOfAssignedCustomers = account.getOwners().size();
				customerDTO.setNumberOfAssignedCustomers(numberOfAssignedCustomers);
			} else {
				customerDTO.setNumberOfAssignedCustomers(0);
			}

			return customerDTO;
		}).collect(Collectors.toList());

		return customerDTOList;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDTO> getCustomerById(Long id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND, id));

		// Ensure collection initialization
		Hibernate.initialize(customer.getAccount());

		// Create the DTO
		CustomerDTO customerDTO = CustomerMapper.toCustomerDTO(customer);

		// Calculate number of assigned customers if there is an associated account
		if (customer.getAccount() != null) {
			int numberOfAssignedCustomers = customer.getAccount().getOwners().size();
			customerDTO.setNumberOfAssignedCustomers(numberOfAssignedCustomers);
		} else {
			customerDTO.setNumberOfAssignedCustomers(0);
		}

		return Optional.of(customerDTO);
	}

	private Account fetchOrCreateAccount(String accountNumber) {
		if (accountNumber == null || accountNumber.isEmpty()) {
			return createDefaultAccount();
		}
		return accountRepository.findByAccountNumber(accountNumber)
				.orElseGet(() -> createAccountWithNumber(accountNumber));
	}

	private Account createDefaultAccount() {
		Account defaultAccount = new Account();
		defaultAccount.setAccountNumber(generateDefaultAccountNumber());
		defaultAccount.setNumberOfOwners(0); // Initialize with 0 owners
		return accountRepository.save(defaultAccount);
	}

	private Account createAccountWithNumber(String accountNumber) {
		Account newAccount = new Account();
		newAccount.setAccountNumber(accountNumber);
		newAccount.setNumberOfOwners(0);
		return accountRepository.save(newAccount);
	}

	private String generateDefaultAccountNumber() {
		return "ACCT" + System.currentTimeMillis();
	}

	private void updateCustomerAddresses(Customer customer, List<AddressDTO> addressDTOs) {
		customer.getAddresses().clear();
		addressDTOs.stream().filter(this::isAddressDTOValid).map(AddressMapper::toEntity)
				.peek(address -> address.setCustomer(customer)).forEach(customer::addAddress);
	}

	private boolean isAddressDTOValid(AddressDTO addressDTO) {
		return addressDTO.getStreet() != null && !addressDTO.getStreet().isEmpty() && addressDTO.getCity() != null
				&& !addressDTO.getCity().isEmpty() && addressDTO.getState() != null && !addressDTO.getState().isEmpty()
				&& addressDTO.getZipCode() != null && !addressDTO.getZipCode().isEmpty()
				&& addressDTO.getCountry() != null && !addressDTO.getCountry().isEmpty();
	}

	private void updateAccountWithCustomer(Account account, Customer customer) {
		account.addOwner(customer);
		accountRepository.save(account);
	}

	private boolean isCustomerDuplicate(CustomerDTO customerDTO, Account account) {
		for (Customer existingCustomer : account.getOwners()) {
			if (existingCustomer.getName().equals(customerDTO.getName())
					&& existingCustomer.getLastname().equals(customerDTO.getLastname())
					&& existingCustomer.getEmail().equals(customerDTO.getEmail())) {
				return true;
			}
		}
		return false;
	}

}