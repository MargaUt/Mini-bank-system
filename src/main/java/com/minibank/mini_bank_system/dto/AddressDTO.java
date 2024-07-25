package com.minibank.mini_bank_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddressDTO {

	public Long id;
	
	public String street;
	public String city;
	public String state;
	public String zipCode;
	public String country;
}
