package com.minibank.mini_bank_system.entities;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.Audited;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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

	@OneToMany(mappedBy = "account", cascade = { CascadeType.DETACH, CascadeType.MERGE })
	@Builder.Default
	private List<Customer> owners = new ArrayList<>();

	public void addOwner(Customer customer) {
		this.owners.add(customer);
	}

}
