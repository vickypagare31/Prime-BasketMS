package com.primebasket.User.entity;

import com.primebasket.User.enums.AddressType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String addressLine;

    private String street;

    private String landmark;

    private String state;

    private String city;

    private String country;

    private String postalCode;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
