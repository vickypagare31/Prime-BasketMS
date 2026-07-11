package com.primebasket.User.service.impl;

import com.primebasket.User.dto.UserRequestDto;
import com.primebasket.User.dto.UserResponseDto;
import com.primebasket.User.entity.User;
import com.primebasket.User.exception.UserNotFoundException;
import com.primebasket.User.mapper.UserMapper;
import com.primebasket.User.repository.UserRepository;
import com.primebasket.User.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        if(userRepository.existsByUserName(userRequestDto.getUserName())){
            throw new RuntimeException("Username already exists");
        }
        if(userRepository.existsByMobileNumber(userRequestDto.getMobileNumber())){
            throw new RuntimeException("Mobile number is already exists");
        }
        User user=UserMapper.requestDtoToEnt(userRequestDto);
        User savedUser=userRepository.save(user);

    }

    @Override
    public Page<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable= PageRequest.of(page, size);
        Page<User>pageList=userRepository.findAll(pageable);
        return pageList.map(UserMapper::entityToDto);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User not found with Id: "+userId));
        return UserMapper.entityToDto(user);


    }

    @Override
    public UserResponseDto updateUserById(Long userId, UserResponseDto userResponseDto) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User not found with this userId: "+userId));

        user.setFullName(userResponseDto.getFullName());
        user.setUserName(userResponseDto.getUserName());
        user.setEmail(userResponseDto.getEmail());
        //user.setPassword(userRequestDto.getPassword());    will think how to change password?
        user.setAddress(userResponseDto.getAddress());
        user.setMobileNumber(userResponseDto.getMobileNumber());

        User updatedUser=userRepository.save(user);
        return UserMapper.entityToDto(updatedUser);
    }

    @Override
    public void deleteUserById(Long userId) {

    }
}
