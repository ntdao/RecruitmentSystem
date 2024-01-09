package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Address;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("""
            select a from Address a
            right join Company c 
            on c.companyAddress.addressId = a.addressId
            where c.companyId = :id
            """)
    Address findByCompanyId(Integer id);

//    @Modifying
//    @Query("""
//            update Address set Address = :address,
//            fullAddress = :fullAddress,
//            ward.wardCode = :wardCode
//            where addressId = :addressId
//            """)
//    void updateAddress(Integer addressId, String address, String fullAddress, String wardCode);

    @Query("""
            select a from Address a
            right join Candidate c
            on c.address.addressId = a.addressId
            """)
    Address findByCandidateId(Integer id);

    @Query("""
            select p.fullName as provinceName, p.provinceCode as provinceCode, d.districtCode as districtCode from provinces p
            join districts d on d.province.provinceCode = p.provinceCode
            join wards w on w.district.districtCode = d.districtCode
            where w.wardCode = :code
            """)
    Tuple getProvinceDistrictByWard(String code);
}
