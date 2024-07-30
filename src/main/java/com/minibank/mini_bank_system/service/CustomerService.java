package com.minibank.mini_bank_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.minibank.mini_bank_system.dto.CustomerDTO;

@Service
public interface CustomerService {

	/**
	 * Creates a new customer in the system. Before creation, it checks if a
	 * customer with the same name, lastname, and email already exists under the
	 * same account to ensure uniqueness. If such a customer exists, a
	 * CustomerAlreadyExistsException is thrown.
	 *
	 * @param customerDTO the data transfer object containing the customer's
	 *                    information
	 * @return the created CustomerDTO object with the number of assigned customers
	 *         in the account
	 */
	CustomerDTO createCustomer(CustomerDTO customerDTO);

	/**
	 * Updates an existing customer's information. This includes updating the
	 * customer's details and their associated addresses.
	 *
	 * @param id          the unique identifier of the customer to update
	 * @param customerDTO the data transfer object containing the updated customer
	 *                    information
	 * @return the updated CustomerDTO object
	 */
	CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

	/**
	 * Retrieves a customer by their ID. If the customer is not found, a
	 * ResourceNotFoundException is thrown.
	 *
	 * @param id the unique identifier of the customer to retrieve
	 * @return an Optional containing the CustomerDTO object if found, or empty if
	 *         not
	 */
	Optional<CustomerDTO> getCustomerById(Long id);

	/**
	 * Searches for customers based on a search term with pagination. This search
	 * covers multiple fields such as name, lastname, and email. The search term is
	 * used to find matching customers, and the method returns a list of CustomerDTO
	 * objects along with the total number of matching customers.
	 *
	 * @param searchTerm the term to search for, which could match various fields
	 * @param page       the zero-based page number to retrieve
	 * @param size       the number of records per page
	 * @return a list of CustomerDTO objects matching the search term and the total
	 *         number of matching customers
	 */
	List<CustomerDTO> searchCustomers(String searchTerm, int page, int size);
}