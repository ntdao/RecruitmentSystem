package com.recruitmentsystem.address.address;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    private Address getCompanyAddress(Integer id) {
        return addressRepository.findByCompanyId(id);
    }

    public AddressResponseModel getCompanyAddressResponseModel(Integer id) {
        return addressMapper.addressToResponseModel(getCompanyAddress(id));
    }

    public void updateAddress(Integer addressId, AddressRequestModel request) {
        Address address = addressRequestModelToEntity(request);
        addressRepository.updateAddress(addressId, address.getAddress(), address.getFullAddress(), address.getWard().getWardCode());
    }

    private Address getUserAddress(Integer id) {
        return addressRepository.findByUserId(id);
    }

    public AddressResponseModel getUserAddressResponseModel(Integer id) {
        return addressMapper.addressToResponseModel(getCompanyAddress(id));
    }

//    private List<Address> getJobAddresses(Integer id) {
//        return addressRepository.findByJobId(id);
//    }
//
//    public List<AddressResponseModel> getJobAddressResponseModel(Integer id) {
//        return getJobAddresses(id).stream()
//                .map(addressMapper::addressToResponseModel)
//                .collect(Collectors.toList());
//    }

    public Address addressRequestModelToEntity(AddressRequestModel request) {
        if (request == null) {
            return new Address();
        }
        Address address = addressMapper.addressRequestModelToAddress(request);
        System.out.println(address);
        return address;
    }

    public Address saveAddress(AddressRequestModel request) {
        return addressRepository.save(addressRequestModelToEntity(request));
    }
}
