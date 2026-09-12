package com.primebasket.User.mapper;

import com.primebasket.User.dto.AddressResponseDto;
import com.primebasket.User.dto.UserAddressResponseDto;
import com.primebasket.User.entity.Address;
import com.primebasket.User.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAddressMapper {

    public static UserAddressResponseDto entToDto(User user){
        UserAddressResponseDto requestDto=new UserAddressResponseDto();

        requestDto.setUserId(user.getUserId());
        requestDto.setFirstName(user.getFirstName());
        requestDto.setLastName(user.getLastName());
        requestDto.setEmail(user.getEmail());
        requestDto.setMobileNumber(user.getMobileNumber());
        requestDto.setActive(user.getIsActive());
        requestDto.setRole(user.getRole());

        List<AddressResponseDto> addresses=new ArrayList<>();

        for(Address address : user.getAddresses()){
            AddressResponseDto addressResponseDto=new AddressResponseDto();

            addressResponseDto.setAddressId(address.getAddressId());
            addressResponseDto.setAddressLine(address.getAddressLine());
            addressResponseDto.setStreet(address.getStreet());
            addressResponseDto.setLandmark(address.getLandmark());
            addressResponseDto.setState(address.getState());
            addressResponseDto.setCity(address.getCity());
            addressResponseDto.setCountry(address.getCountry());
            addressResponseDto.setPostalCode(address.getPostalCode());
            addressResponseDto.setAddressType(address.getAddressType());
            addressResponseDto.setIsDefault(address.getIsDefault());
            addressResponseDto.setUserId(user.getUserId());

            addresses.add(addressResponseDto);
        }

        requestDto.setAddressList(addresses);
        return  requestDto;
    }

}
