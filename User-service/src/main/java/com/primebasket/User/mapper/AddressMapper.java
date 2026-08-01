package com.primebasket.User.mapper;

import com.primebasket.User.dto.AddressRequestDto;
import com.primebasket.User.dto.AddressResponseDto;
import com.primebasket.User.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    /**
     *
     * @param address
     * @return responseDto
     */

    public static AddressResponseDto entToDto(Address address){

        AddressResponseDto responseDto=new AddressResponseDto();

        responseDto.setAddressId(address.getAddressId());
        responseDto.setAddressLine(address.getAddressLine());
        responseDto.setAddressType(address.getAddressType());
        responseDto.setCity(address.getCity());
        responseDto.setCountry(address.getCountry());
        responseDto.setState(address.getState());
        responseDto.setLandmark(address.getLandmark());
        responseDto.setStreet(address.getStreet());
        responseDto.setPostalCode(address.getPostalCode());
        responseDto.setIsDefault(address.getIsDefault());
        responseDto.setUserId(address.getUser().getUserId());

        return responseDto;

    }

    /**
     *
     * @param requestDto
     * @return address
     */
    public static Address dtoToEnt(AddressRequestDto requestDto){
        Address address=new Address();

        address.setAddressLine(requestDto.getAddressLine());
        address.setAddressType(requestDto.getAddressType());
        address.setCity(requestDto.getCity());
        address.setCountry(requestDto.getCountry());
        address.setState(requestDto.getState());
        address.setLandmark(requestDto.getLandmark());
        address.setStreet(requestDto.getStreet());
        address.setPostalCode(requestDto.getPostalCode());
        address.setIsDefault(requestDto.getIsDefault());
        return address;
    }


}
