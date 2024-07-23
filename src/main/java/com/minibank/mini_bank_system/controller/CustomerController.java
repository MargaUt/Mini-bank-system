package com.minibank.mini_bank_system.controller;

import com.minibank.mini_bank_system.dto.CustomerDTO;
import com.minibank.mini_bank_system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

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

	@GetMapping("/search")
	public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String searchTerm, @RequestParam int page,
			@RequestParam int size) {
		List<CustomerDTO> customers = customerService.searchCustomers(searchTerm, page, size);
		return ResponseEntity.ok(customers);
	}
}