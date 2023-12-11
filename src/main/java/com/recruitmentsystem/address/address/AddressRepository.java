package com.recruitmentsystem.address.address;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("""
            select a from address a
            right join Company c 
            on c.companyAddress.addressId = a.addressId
            where c.companyId = :id
            """)
    Address findByCompanyId(Integer id);

    @Modifying
    @Query("""
            update address set address = :address,
            fullAddress = :fullAddress,
            ward.wardCode = :wardCode
            where addressId = :addressId
            """)
    void updateAddress(Integer addressId, String address, String fullAddress, String wardCode);

    @Query("""
            select a from address a
            right join User u
            on u.address.addressId = a.addressId
            """)
    Address findByUserId(Integer id);

    @Query("""
            select p.fullName as provinceName, p.provinceCode as provinceCode, d.districtCode as districtCode from provinces p
            join districts d on d.province.provinceCode = p.provinceCode
            join wards w on w.district.districtCode = d.districtCode
            where w.wardCode = :code
            """)
    Tuple getProvinceDistrictByWard(String code);
}
