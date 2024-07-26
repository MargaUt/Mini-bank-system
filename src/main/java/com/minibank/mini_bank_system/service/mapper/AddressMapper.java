package com.minibank.mini_bank_system.service.mapper;

import com.minibank.mini_bank_system.dto.AddressDTO;
import com.minibank.mini_bank_system.entities.Address;

public class AddressMapper {

	public static AddressDTO toDTO(Address address) {
		return AddressDTO.builder().id(address.getId()).street(address.getStreet()).city(address.getCity())
				.state(address.getState()).zipCode(address.getZipCode()).country(address.getCountry()).build();
	}

	public static Address toEntity(AddressDTO addressDTO) {
		return Address.builder().street(addressDTO.getStreet()).city(addressDTO.getCity()).state(addressDTO.getState())
				.zipCode(addressDTO.getZipCode()).country(addressDTO.getCountry()).build();
	}
}
