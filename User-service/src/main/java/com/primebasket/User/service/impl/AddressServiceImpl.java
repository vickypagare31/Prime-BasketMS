package com.primebasket.User.service.impl;

import com.primebasket.User.dto.AddressRequestDto;
import com.primebasket.User.dto.AddressResponseDto;
import com.primebasket.User.entity.Address;
import com.primebasket.User.entity.User;
import com.primebasket.User.exception.UserNotFoundException;
import com.primebasket.User.mapper.AddressMapper;
import com.primebasket.User.repository.AddressRepository;
import com.primebasket.User.repository.UserRepository;
import com.primebasket.User.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    private static final Logger logger= LoggerFactory.getLogger(AddressServiceImpl.class);

    @Override
    public AddressResponseDto addAddress(Long userId,AddressRequestDto addressRequestDto) {

        User user= userRepository.findById(userId).orElseThrow(()->
                new UserNotFoundException("User not found with given Id: "+userId));

        Address address= AddressMapper.dtoToEnt(addressRequestDto);
        address.setUser(user);
        Address savedAddress=addressRepository.save(address);

        logger.info("Address registered successfully with userId", user.getUserId());
        return AddressMapper.entToDto(savedAddress);
    }
}
