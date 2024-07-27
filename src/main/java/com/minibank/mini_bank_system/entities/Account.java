package com.minibank.mini_bank_system.entities;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Audited
public class Account extends BaseEntity {

	private String accountNumber;

	private int numberOfOwners;

	@ManyToMany(mappedBy = "accounts", cascade = { CascadeType.DETACH, CascadeType.MERGE })
	@Builder.Default
	@JsonIgnore
	private Set<Customer> owners = new HashSet<>();

	public void addOwner(Customer customer) {
		this.owners.add(customer);
	}

}
