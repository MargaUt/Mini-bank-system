package com.minibank.mini_bank_system.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.envers.Audited;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Audited
@Table(name = "Customer", indexes = {
		@Index(name = "idx_name_lastname", columnList = "name, lastname", unique = true) })
public class Customer extends BaseEntity {

	@NotEmpty(message = "Name may not be empty")
	@Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters long")
	public String name;

	@NotEmpty(message = "Lastname may not be empty")
	@Size(min = 2, max = 32, message = "Lastname must be between 2 and 32 characters long")
	public String lastname;

	@Pattern(regexp = "(^$|[0-9]{10})")
	public String phoneNumber;

	@Email
	public String email;

	@Enumerated(EnumType.STRING)
	public CustomerType customerType;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Builder.Default
	private List<Address> addresses = new ArrayList<>();

	@ManyToMany(mappedBy = "owners")
	@Builder.Default
	private Set<Account> accounts = new HashSet<>();

	public void addAddress(Address address) {
		addresses.add(address);
		address.setCustomer(this);
	}

	public void addAccount(Account account) {
		if (accounts.add(account)) {
			account.getOwners().add(this);
		}
	}

}
