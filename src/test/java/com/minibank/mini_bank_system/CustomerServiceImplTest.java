package com.minibank.mini_bank_system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.minibank.mini_bank_system.dto.CustomerDTO;
import com.minibank.mini_bank_system.entities.Account;
import com.minibank.mini_bank_system.entities.Customer;
import com.minibank.mini_bank_system.entities.CustomerType;
import com.minibank.mini_bank_system.repository.AccountRepository;
import com.minibank.mini_bank_system.repository.CustomerRepository;
import com.minibank.mini_bank_system.service.impl.CustomerServiceImpl;

public class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private CustomerServiceImpl customerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createCustomer_ShouldCreateCustomer() {
		// Arrange
		CustomerDTO customerDTO = CustomerDTO.builder().name("John").lastname("Doe").phoneNumber("1234567890")
				.email("john.doe@example.com").customerType(CustomerType.INDIVIDUAL).addresses(Collections.emptyList())
				.accountIds(Set.of(1L, 2L)).build();

		// Create Account entities for the test
		Account account1 = Account.builder().accountNumber("ACCT1").numberOfOwners(1).build();
		account1.setId(1L); // Correctly set ID for account1

		Account account2 = Account.builder().accountNumber("ACCT2").numberOfOwners(1).build();
		account2.setId(2L); // Correctly set ID for account2

		// Create a Customer entity as expected to be returned from the service
		Customer customer = Customer.builder().id(1L).name("John").lastname("Doe").phoneNumber("1234567890")
				.email("john.doe@example.com").customerType(CustomerType.INDIVIDUAL).addresses(new ArrayList<>())
				.accounts(Set.of(account1, account2)).build();

		// Mock repository interactions
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
		when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

		// Act
		CustomerDTO result = customerService.createCustomer(customerDTO);

		// Assert
		assertNotNull(result);
		assertEquals(customerDTO.getName(), result.getName());
		assertEquals(customerDTO.getLastname(), result.getLastname());
		assertEquals(customerDTO.getPhoneNumber(), result.getPhoneNumber());
		assertEquals(customerDTO.getEmail(), result.getEmail());
		assertEquals(customerDTO.getCustomerType(), result.getCustomerType());

		// Ensure account IDs are correctly compared
		assertEquals(customerDTO.getAccountIds(), result.getAccountIds());
	}

	@Test
	void updateCustomer_ShouldUpdateCustomer() {
		// Arrange
		Long customerId = 1L;
		CustomerDTO updatedCustomerDTO = CustomerDTO.builder().name("Jane").lastname("Doe").phoneNumber("0987654321")
				.email("jane.doe@example.com").customerType(CustomerType.PUBLIC).addresses(Collections.emptyList())
				.accountIds(Set.of(3L)).build();

		// Existing customer with different CustomerType
		Customer existingCustomer = Customer.builder().id(customerId).name("John").lastname("Doe")
				.phoneNumber("1234567890").email("john.doe@example.com").customerType(CustomerType.PRIVATE)
				.addresses(new ArrayList<>()).accounts(new HashSet<>()).build();

		// Updated customer that is expected after the update
		Customer updatedCustomer = Customer.builder().id(customerId).name("Jane").lastname("Doe")
				.phoneNumber("0987654321").email("jane.doe@example.com").customerType(CustomerType.PUBLIC)
				.addresses(new ArrayList<>()).accounts(new HashSet<>()).build();

		// Mock repository interactions
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
		when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

		// Act
		CustomerDTO result = customerService.updateCustomer(customerId, updatedCustomerDTO);

		// Assert
		assertNotNull(result);
		assertEquals(updatedCustomerDTO.getName(), result.getName());
		assertEquals(updatedCustomerDTO.getLastname(), result.getLastname());
		assertEquals(updatedCustomerDTO.getPhoneNumber(), result.getPhoneNumber());
		assertEquals(updatedCustomerDTO.getEmail(), result.getEmail());
		assertEquals(updatedCustomerDTO.getCustomerType(), result.getCustomerType());
	}

	@Test
	void getCustomerById_ShouldReturnCustomer() {
		// Arrange
		Long customerId = 1L;
		Customer customer = Customer.builder().id(customerId).name("John").lastname("Doe").phoneNumber("1234567890")
				.email("john.doe@example.com").customerType(CustomerType.INDIVIDUAL).addresses(new ArrayList<>())
				.accounts(new HashSet<>()).build();

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

		// Act
		Optional<CustomerDTO> result = customerService.getCustomerById(customerId);

		// Assert
		assertTrue(result.isPresent());
		assertEquals(customerId, result.get().getId());
		assertEquals(customer.getName(), result.get().getName());
	}

}
