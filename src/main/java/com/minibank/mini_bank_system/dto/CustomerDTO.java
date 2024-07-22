package com.minibank.mini_bank_system.dto;

import com.minibank.mini_bank_system.entities.CustomerType;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class CustomerDTO {

    public Long id;
    
    public String name;
    public String lastname;
    public String phoneNumber;
    public String email;
    public CustomerType customerType;
    
    
}
