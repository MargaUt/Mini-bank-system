package com.minibank.mini_bank_system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minibank.mini_bank_system.dto.AddressDTO;
import com.minibank.mini_bank_system.dto.CustomerDTO;
import com.minibank.mini_bank_system.entities.Address;
import com.minibank.mini_bank_system.entities.Customer;
import com.minibank.mini_bank_system.exception.ResourceNotFoundException;
import com.minibank.mini_bank_system.repository.CustomerRepository;
import com.minibank.mini_bank_system.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	// TODO:
	// Implement several tests for CustomerServiceImpl

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	@Transactional
	public CustomerDTO createCustomer(CustomerDTO customerDTO) {
		Customer customer = convertToEntity(customerDTO);

		// Handle addresses
		for (AddressDTO addressDTO : customerDTO.getAddresses()) {
			Address address = convertToEntity(addressDTO);
			address.setCustomer(customer);
			customer.addAddress(address);
		}

		customer = customerRepository.save(customer);
		return convertToDTO(customer);
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

	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDTO> getCustomerById(Long id) {
		return customerRepository.findById(id).map(this::convertToDTO);
	}

	private CustomerDTO convertToDTO(Customer customer) {
		CustomerDTO dto = new CustomerDTO();
		dto.setId(customer.getId());
		dto.setName(customer.getName());
		dto.setLastname(customer.getLastname());
		dto.setPhoneNumber(customer.getPhoneNumber());
		dto.setEmail(customer.getEmail());
		dto.setCustomerType(customer.getCustomerType());

		// Convert addresses to AddressDTO
		List<AddressDTO> addressDTOs = customer.getAddresses().stream().map(this::convertToDTO)
				.collect(Collectors.toList());
		dto.setAddresses(addressDTOs);

		return dto;
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

	private Customer convertToEntity(CustomerDTO customerDTO) {
		List<Address> addresses = customerDTO.getAddresses() == null ? new ArrayList<>()
				: customerDTO.getAddresses().stream().map(this::convertToEntity).collect(Collectors.toList());

		return Customer.builder().name(customerDTO.getName()).lastname(customerDTO.getLastname())
				.phoneNumber(customerDTO.getPhoneNumber()).email(customerDTO.getEmail())
				.customerType(customerDTO.getCustomerType()).addresses(addresses).build();
	}

	private Address convertToEntity(AddressDTO addressDTO) {
		return Address.builder().street(addressDTO.getStreet()).city(addressDTO.getCity()).state(addressDTO.getState())
				.zipCode(addressDTO.getZipCode()).country(addressDTO.getCountry()).build();
	}

}