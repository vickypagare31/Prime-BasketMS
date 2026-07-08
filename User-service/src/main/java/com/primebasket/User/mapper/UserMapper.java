package com.primebasket.User.mapper;

import com.primebasket.User.dto.UserRequestDto;
import com.primebasket.User.dto.UserResponseDto;
import com.primebasket.User.entity.User;
import org.springframework.stereotype.Component;

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

        userResponseDto.setFullName(user.getFullName());
        userResponseDto.setUserName(user.getUserName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setAddress(user.getAddress());
        userResponseDto.setMobileNumber(user.getMobileNumber());

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
        user.setUserId(requestDto.getUserId());
        user.setFullName(requestDto.getFullName());
        user.setUserName(requestDto.getUserName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setAddress(requestDto.getAddress());
        user.setMobileNumber(requestDto.getMobileNumber());

        return user;

    }
}
