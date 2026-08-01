package com.primebasket.User.service;

import com.primebasket.User.dto.UserRequestDto;
import com.primebasket.User.dto.UserResponseDto;
import com.primebasket.User.entity.User;
import com.primebasket.User.exception.EmailAlreadyExistsException;
import com.primebasket.User.exception.MobileNoAlreadyExistsException;
import com.primebasket.User.repository.UserRepository;
import com.primebasket.User.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDto requestDto;
    private User user;

    @BeforeEach
    void setUp() {

        requestDto = new UserRequestDto();

        requestDto.setFirstName("Mahesh");
        requestDto.setLastName("Rahane");
        requestDto.setEmail("mahesh@gmail.com");
        requestDto.setMobileNumber("9876543210");
        requestDto.setPassword("Password@123");

        user = new User();

        user.setUserId(1L);
        user.setFirstName("Mahesh");
        user.setLastName("Rahane");
        user.setEmail("mahesh@gmail.com");
        user.setMobileNumber("9876543210");
    }

    @Test
    void shouldRegisterUserSuccessfully() {

        when(userRepository.existsByEmail(requestDto.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByMobileNumber(requestDto.getMobileNumber()))
                .thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponseDto response = userService.registerUser(requestDto);

        assertNotNull(response);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(requestDto.getEmail()))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.registerUser(requestDto)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenMobileAlreadyExists() {

        when(userRepository.existsByEmail(requestDto.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByMobileNumber(requestDto.getMobileNumber()))
                .thenReturn(true);

        assertThrows(
                MobileNoAlreadyExistsException.class,
                () -> userService.registerUser(requestDto)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }
}

