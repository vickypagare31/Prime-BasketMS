package com.primebasket.User.dto;

import com.primebasket.User.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {


    private String firstName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private Role role;

    private Boolean active;

    private Boolean emailVerified;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;
}
