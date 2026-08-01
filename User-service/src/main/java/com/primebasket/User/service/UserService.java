package com.primebasket.User.service;

import com.primebasket.User.dto.UserAddressResponseDto;
import com.primebasket.User.dto.UserRequestDto;
import com.primebasket.User.dto.UserResponseDto;
import com.primebasket.User.dto.UserUpdateDto;
import org.springframework.data.domain.Page;

public interface UserService {

        UserResponseDto registerUser(UserRequestDto userRequestDto);

        Page<UserResponseDto> getAllUsers(int page, int size);

        UserAddressResponseDto getUserById(Long userId);

        UserUpdateDto updateUserById(Long userId, UserUpdateDto userUpdateDto);

        void deleteUserById(Long userId);
}
