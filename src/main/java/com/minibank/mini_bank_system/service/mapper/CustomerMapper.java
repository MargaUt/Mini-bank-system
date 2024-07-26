package com.minibank.mini_bank_system.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.minibank.mini_bank_system.dto.AddressDTO;
import com.minibank.mini_bank_system.dto.CustomerDTO;
import com.minibank.mini_bank_system.entities.Account;
import com.minibank.mini_bank_system.entities.Address;
import com.minibank.mini_bank_system.entities.Customer;

public class CustomerMapper {

    public static Customer toCustomer(CustomerDTO customerDTO, Set<Account> accounts) {
        return Customer.builder()
                .id(customerDTO.getId())
                .name(customerDTO.getName())
                .lastname(customerDTO.getLastname())
                .phoneNumber(customerDTO.getPhoneNumber())
                .email(customerDTO.getEmail())
                .customerType(customerDTO.getCustomerType())
                .addresses(customerDTO.getAddresses().stream()
                        .map(CustomerMapper::toAddress)
                        .collect(Collectors.toList()))
                .accounts(accounts)
                .build();
    }

    public static CustomerDTO toCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .lastname(customer.getLastname())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .customerType(customer.getCustomerType())
                .addresses(customer.getAddresses().stream()
                        .map(CustomerMapper::toAddressDTO)
                        .collect(Collectors.toList()))
                .accountIds(customer.getAccounts().stream()
                        .map(Account::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static Address toAddress(AddressDTO addressDTO) {
        return Address.builder()
                .street(addressDTO.getStreet())
                .city(addressDTO.getCity())
                .state(addressDTO.getState())
                .zipCode(addressDTO.getZipCode())
                .country(addressDTO.getCountry())
                .build();
    }

    public static AddressDTO toAddressDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .build();
    }
}
