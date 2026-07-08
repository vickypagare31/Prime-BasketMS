package com.primebasket.User.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequestDto {

    private Long userId;

    private String fullName;

    private String userName;

    private String email;

    private String address;

    private String password;

    private String mobileNumber;
}
