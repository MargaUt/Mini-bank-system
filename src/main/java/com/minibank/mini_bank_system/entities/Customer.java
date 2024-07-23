package com.minibank.mini_bank_system.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Customer extends BaseEntity {
//TODO:
// finish basic Customer CRUD,
// Introduce Swagger, 
// Implement several tests for CustomerServiceImpl

	public String name;

	public String lastname;

	public String phoneNumber;

	public String email;

	@Enumerated(EnumType.STRING)
	public CustomerType customerType;

}
