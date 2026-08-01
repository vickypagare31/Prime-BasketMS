package com.primebasket.User.dto;

import com.primebasket.User.entity.Address;
import com.primebasket.User.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressResponseDto {

    private Long addressId;

    private String addressLine;

    private String street;

    private String landmark;

    private String state;

    private String city;

    private String country;

    private String postalCode;

    private AddressType addressType;

    private Boolean isDefault;

    private Long userId;

}
