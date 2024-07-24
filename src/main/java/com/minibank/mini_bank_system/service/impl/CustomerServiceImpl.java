package com.minibank.mini_bank_system.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.minibank.mini_bank_system.dto.CustomerDTO;
import com.minibank.mini_bank_system.entities.Customer;
import com.minibank.mini_bank_system.repository.CustomerRepository;
import com.minibank.mini_bank_system.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public CustomerDTO createCustomer(CustomerDTO customerDTO) {
		// Check if customer already exists
		boolean exists = customerRepository.existsByNameAndLastnameAndPhoneNumberAndEmail(customerDTO.getName(),
				customerDTO.getLastname(), customerDTO.getPhoneNumber(), customerDTO.getEmail());

		if (exists) {
			throw new RuntimeException("Customer with the same details already exists.");
		}

		Customer customer = convertToEntity(customerDTO);
		Customer savedCustomer = customerRepository.save(customer);
		return convertToDTO(savedCustomer);
	}

	@Override
	public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
		Optional<Customer> existingCustomerOpt = customerRepository.findById(id);

		if (!existingCustomerOpt.isPresent()) {
			throw new RuntimeException("Customer not found.");
		}

		Customer existingCustomer = existingCustomerOpt.get();
		existingCustomer.setName(customerDTO.getName());
		existingCustomer.setLastname(customerDTO.getLastname());
		existingCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
		existingCustomer.setEmail(customerDTO.getEmail());
		existingCustomer.setCustomerType(customerDTO.getCustomerType());

		Customer updatedCustomer = customerRepository.save(existingCustomer);
		return convertToDTO(updatedCustomer);
	}

	@Override
	public Optional<CustomerDTO> getCustomerById(Long id) {
		return customerRepository.findById(id).map(this::convertToDTO);
	}

	@Override
	public List<CustomerDTO> searchCustomers(String searchTerm, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Customer> customerPage = customerRepository.findAllByNameContainingOrEmailContaining(searchTerm,
				searchTerm, pageable);
		return customerPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// Convert Entity to DTO
	private CustomerDTO convertToDTO(Customer customer) {
		return CustomerDTO.builder().id(customer.getId()).name(customer.getName()).lastname(customer.getLastname())
				.phoneNumber(customer.getPhoneNumber()).email(customer.getEmail())
				.customerType(customer.getCustomerType()).build();
	}

	// Convert DTO to Entity
	private Customer convertToEntity(CustomerDTO customerDTO) {
		return Customer.builder().name(customerDTO.getName()).lastname(customerDTO.getLastname())
				.phoneNumber(customerDTO.getPhoneNumber()).email(customerDTO.getEmail())
				.customerType(customerDTO.getCustomerType()).build();
	}

}