package com.recruitmentsystem.service;

import com.recruitmentsystem.dto.AddressDto;
import com.recruitmentsystem.entity.Address;
import com.recruitmentsystem.mapper.AddressMapper;
import com.recruitmentsystem.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    private Address getCompanyAddress(Integer id) {
        return addressRepository.findByCompanyId(id);
    }

    public AddressDto getCompanyAddressResponseModel(Integer id) {
        return addressMapper.entityToDto(getCompanyAddress(id));
    }

    public Address updateAddress(Integer addressId, AddressDto request) {
        Address address = addressRequestModelToEntity(request);
        address.setAddressId(addressId);
        return addressRepository.save(address);
    }

    private Address getCandidateAddress(Integer id) {
        return addressRepository.findByCandidateId(id);
    }

    public AddressDto getCandidateAddressResponseModel(Integer id) {
        return addressMapper.entityToDto(getCompanyAddress(id));
    }

    public Address addressRequestModelToEntity(AddressDto request) {
        return request == null ? null : addressMapper.dtoToEntity(request);
    }

    public Address saveAddress(AddressDto request) {
        return addressRepository.save(addressRequestModelToEntity(request));
    }
}
