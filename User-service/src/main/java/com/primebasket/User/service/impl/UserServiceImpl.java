package com.primebasket.User.service.impl;

import com.primebasket.User.dto.*;
import com.primebasket.User.entity.User;
import com.primebasket.User.enums.Role;
import com.primebasket.User.exception.EmailAlreadyExistsException;
import com.primebasket.User.exception.MobileNoAlreadyExistsException;
import com.primebasket.User.exception.UserAlreadyDeactivatedException;
import com.primebasket.User.exception.UserNotFoundException;
import com.primebasket.User.mapper.UserAddressMapper;
import com.primebasket.User.mapper.UserMapper;
import com.primebasket.User.mapper.UserUpdateMapper;
import com.primebasket.User.repository.UserRepository;
import com.primebasket.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserAddressMapper userAddressMapper;
    private final UserUpdateMapper userUpdateMapper;


    private static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            logger.warn("Registration failed. Email already exists:", userRequestDto.getEmail());

            throw new EmailAlreadyExistsException("Email already exists");
        }
        if(userRepository.existsByMobileNumber(userRequestDto.getMobileNumber())){
            logger.warn("Registration failed. Mobile Number already exists:", userRequestDto.getMobileNumber());

            throw new MobileNoAlreadyExistsException("Mobile number is already exists");
        }
        User user=UserMapper.requestDtoToEnt(userRequestDto);
        user.setRole(Role.CUSTOMER);
        user.setIsActive(true);
        user.setEmailVerified(false);
        User savedUser=userRepository.save(user);

        logger.info("User registered successfully. User ID: {}",savedUser.getUserId());

        return UserMapper.entityToDto(savedUser);

    }

    @Override
    public Page<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable= PageRequest.of(page, size);
        Page<User>pageList=userRepository.findAll(pageable);
        return pageList.map(UserMapper::entityToDto);
    }

    @Override
    public UserAddressResponseDto getUserById(Long userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User not found with Id: "+userId));

        logger.warn("User not found with userId", userId);
        return UserAddressMapper.entToDto(user);


    }

    @Override
    public UserUpdateDto updateUserById(Long userId, UserUpdateDto userUpdateDto) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User not found with this userId: "+userId));

        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setMobileNumber(userUpdateDto.getMobileNumber());
        User updatedUser=userRepository.save(user);
        return UserUpdateMapper.entToDto(updatedUser);
    }

    @Override
    public void deleteUserById(Long userId) {

        User user=userRepository.findById(userId).orElseThrow(()->
                new UserNotFoundException("User not found with Id:"+userId));

        if(Boolean.FALSE.equals(user.getIsActive())){
            throw new UserAlreadyDeactivatedException("User with Id: %d already deactivated:".formatted(userId));
        }
        user.setIsActive(Boolean.valueOf("false"));
        userRepository.save(user);

    }
}
