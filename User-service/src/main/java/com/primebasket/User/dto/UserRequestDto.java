package com.primebasket.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp = "^(\\+91|91)?[6-9]\\d{9}$",
            message = "Invalid mobile number")
    private String mobileNumber;


}
