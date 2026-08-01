package com.primebasket.User.dto;

import com.primebasket.User.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressRequestDto {

    @NotBlank(message = "Address is required")
    private String addressLine;

    private String street;

    private String landmark;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Postal Code is required")
    private String postalCode;

    private Boolean isDefault;

    private AddressType addressType;
}
