package com.minibank.mini_bank_system.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
public class Customer extends BaseEntity {

	public String name;

	public String lastname;

	public String phoneNumber;

	public String email;

	@Enumerated(EnumType.STRING)
	public CustomerType customerType;

}
