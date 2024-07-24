package com.minibank.mini_bank_system.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minibank.mini_bank_system.dto.CustomerDTO;
import com.minibank.mini_bank_system.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping
	public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
		CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
		return ResponseEntity.ok(createdCustomer);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
		CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
		return ResponseEntity.ok(updatedCustomer);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
		Optional<CustomerDTO> customerDTO = customerService.getCustomerById(id);
		return customerDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/customers/search")
	public List<CustomerDTO> searchCustomers(@RequestParam String searchTerm, @RequestParam int page,
			@RequestParam int size) {
		return customerService.searchCustomers(searchTerm, page, size);
	}
}