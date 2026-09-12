package com.primebasket.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAddressResponseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private Role role;

    private Boolean active;

    private List<AddressResponseDto>addressList;
}
