package com.primebasket.User.service;

import com.primebasket.User.dto.UserRequestDto;
import com.primebasket.User.dto.UserResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

        void createUser(UserRequestDto userRequestDto);

        Page<UserResponseDto> getAllUsers(int page, int size);

        UserResponseDto getUserById(Long userId);

        UserResponseDto updateUserById(Long userId, UserResponseDto userResponseDto);

        void deleteUserById(Long userId);
}
