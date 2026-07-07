package com.primebasket.User.service.impl;

import com.primebasket.User.dto.UserRequestDto;
import com.primebasket.User.entity.User;
import com.primebasket.User.mapper.UserMapper;
import com.primebasket.User.repository.UserRepository;
import com.primebasket.User.service.UserService;
import org.springframework.stereotype.Service;

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
        User user=UserMapper.requestDtoToEnt(userRequestDto);
        User savedUser=userRepository.save(user);

    }
}
