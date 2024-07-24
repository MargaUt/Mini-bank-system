package com.minibank.mini_bank_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.minibank.mini_bank_system.dto.CustomerDTO;

@Service
public interface CustomerService {

    /**
     * Create a new customer.
     * @param customerDTO the customer data to create
     * @return the created customer
     */
    CustomerDTO createCustomer(CustomerDTO customerDTO);

    /**
     * Update an existing customer.
     * @param id the ID of the customer to update
     * @param customerDTO the customer data to update
     * @return the updated customer
     */
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    /**
     * Get a customer by ID.
     * @param id the ID of the customer to retrieve
     * @return the customer with the given ID
     */
    Optional<CustomerDTO> getCustomerById(Long id);

    /**
     * TODO: Adjust search by the requirements:
     * Get customers by search term
The idea is that in the bank there are a lot of customers with same name, lastname, age,
customer type, city or street.
We should be able to find them by search term.
Page request - pagination.
Request consist of search term (example: lastname), page and row number.
Response has to contain list of customers and their total number.

     * Search for customers based on a search term with pagination.
     * @param searchTerm the term to search by
     * @param page the page number
     * @param size the number of records per page
     * @return a list of customers matching the search term
     */
    List<CustomerDTO> searchCustomers(String searchTerm, int page, int size);
}

