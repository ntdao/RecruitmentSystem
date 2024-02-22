package hust.seee.recruitmentsystem.service;

import hust.seee.recruitmentsystem.dto.AddressDTO;
import hust.seee.recruitmentsystem.entity.Address;
import hust.seee.recruitmentsystem.mapper.AddressMapper;
import hust.seee.recruitmentsystem.repository.AddressRepository;
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

    public AddressDTO getCompanyAddressResponseModel(Integer id) {
        return addressMapper.entityToDto(getCompanyAddress(id));
    }

    public Address updateAddress(Integer addressId, AddressDTO request) {
        Address address = addressRequestModelToEntity(request);
        address.setAddressId(addressId);
        return addressRepository.save(address);
    }

    private Address getCandidateAddress(Integer id) {
        return addressRepository.findByCandidateId(id);
    }

    public AddressDTO getCandidateAddressResponseModel(Integer id) {
        return addressMapper.entityToDto(getCompanyAddress(id));
    }

    public Address addressRequestModelToEntity(AddressDTO request) {
        return request == null ? null : addressMapper.dtoToEntity(request);
    }

    public Address saveAddress(AddressDTO request) {
        return addressRepository.save(addressRequestModelToEntity(request));
    }
}
