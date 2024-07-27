package com.minibank.mini_bank_system.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.minibank.mini_bank_system.dto.CustomerDTO;
import com.minibank.mini_bank_system.exception.ResourceNotFoundException;
import com.minibank.mini_bank_system.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * Create a new customer.
	 *
	 * @param customerDTO the customer details to create
	 * @return the created customer with a 201 Created status
	 */
	@PostMapping
	public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
		CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
	}

	/**
	 * Update an existing customer by ID.
	 *
	 * @param id          the ID of the customer to update
	 * @param customerDTO the updated customer details
	 * @return the updated customer with a 200 OK status, or a 404 Not Found if the
	 *         customer does not exist
	 */
	@PutMapping("/{id}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
		try {
			CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
			return ResponseEntity.ok(updatedCustomer);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	/**
	 * Retrieve a customer by ID.
	 * 
	 * @param id the ID of the customer to retrieve
	 * @return the customer with a 200 OK status, or a 404 Not Found if the customer
	 *         does not exist
	 */
	@GetMapping("/{id}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
		Optional<CustomerDTO> customerDTO = customerService.getCustomerById(id);
		return customerDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	/**
	 * Search for customers based on search term and pagination.
	 * 
	 * @param searchTerm the term to search for
	 * @param page       the page number to retrieve
	 * @param size       the number of customers per page
	 * @return a list of customers matching the search term with a 200 OK status
	 */
	@GetMapping("/search")
	public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String searchTerm, @RequestParam int page,
			@RequestParam int size) {
		List<CustomerDTO> customers = customerService.searchCustomers(searchTerm, page, size);
		return ResponseEntity.ok(customers);
	}
}
