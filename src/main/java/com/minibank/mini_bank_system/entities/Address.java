package com.minibank.mini_bank_system.entities;

import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class Address extends BaseEntity {

	// TODO improve id generation strategy
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// TODO improve fields naming
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private String country;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
}
