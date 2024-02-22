package hust.seee.recruitmentsystem.mapper;

import hust.seee.recruitmentsystem.dto.AddressDTO;
import hust.seee.recruitmentsystem.entity.Address;
import hust.seee.recruitmentsystem.entity.Ward;
import hust.seee.recruitmentsystem.repository.AddressRepository;
import hust.seee.recruitmentsystem.service.DistrictService;
import hust.seee.recruitmentsystem.service.ProvinceService;
import hust.seee.recruitmentsystem.service.WardService;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressMapper {
    private final AddressRepository addressRepository;
    private final DistrictService districtService;
    private final ProvinceService provinceService;
    private final WardService wardService;

    public AddressDTO entityToDto(Address address) {
        if (address == null || address.getWard() == null) {
            return null;
        }
        String wardCode = address.getWard().getWardCode();
        Tuple tuple = addressRepository.getProvinceDistrictByWard(wardCode);
        return AddressDTO
                .builder()
                .id(address.getAddressId())
                .provinceCode(tuple.get("provinceCode", String.class))
                .province(tuple.get("provinceName", String.class))
                .districtCode(tuple.get("districtCode", String.class))
                .wardCode(wardCode)
                .address(address.getAddress())
                .fullAddress(address.getFullAddress())
                .build();
    }

    public Address dtoToEntity(AddressDTO request) {
        if (request == null) return null;
        Ward ward = wardService.findWardByWardCode(request.getWardCode());
        String fullAddress = request.getAddress() + ", "
                + ward.getFullName() + ", "
                + districtService.findDistrictByDistrictCode(request.getDistrictCode()) + ", "
                + provinceService.findProvinceByProvinceCode(request.getProvinceCode());
        return Address
                .builder()
                .ward(ward)
                .address(request.getAddress())
                .fullAddress(fullAddress)
                .build();
    }
}
