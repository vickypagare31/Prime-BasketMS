package com.primebasket.User.mapper;

import com.primebasket.User.dto.*;
import com.primebasket.User.entity.Address;
import com.primebasket.User.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    /**
     *
     * @param user
     * @return User Dto
     * User Response
     */
    public static UserResponseDto entityToDto(User user){

        UserResponseDto userResponseDto =new UserResponseDto();

        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setMobileNumber(user.getMobileNumber());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setActive(user.getIsActive());
        userResponseDto.setEmailVerified(user.getEmailVerified());
        userResponseDto.setCreateAt(user.getCreatedAt());
        userResponseDto.setUpdatedAt(user.getUpdatedAt());

        return userResponseDto;
    }

    /**
     *
     * @param requestDto
     * @return user
     * User Request
     */
    public static User requestDtoToEnt(UserRequestDto requestDto){

        User user=new User();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setMobileNumber(requestDto.getMobileNumber());
        user.setPassword(requestDto.getPassword());

        return user;

    }
}
