package com.minibank.mini_bank_system.entities;

import java.time.LocalDateTime;

import org.hibernate.envers.Audited;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass
@Audited
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    public int versionNum;
    public String createdBy;
    public LocalDateTime creationDate;
    public String lastModifiedBy;
    public LocalDateTime lastModifiedDate;

}
