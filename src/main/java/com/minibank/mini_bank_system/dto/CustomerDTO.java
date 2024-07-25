package com.minibank.mini_bank_system.dto;

import java.util.List;
import java.util.Set;

import com.minibank.mini_bank_system.entities.CustomerType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerDTO {

	public Long id;

	public String name;
	public String lastname;
	public String phoneNumber;
	public String email;
	public CustomerType customerType;
	public List<AddressDTO> addresses;

	private Set<Long> accountIds;

}
