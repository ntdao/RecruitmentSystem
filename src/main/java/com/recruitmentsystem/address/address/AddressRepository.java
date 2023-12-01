package com.recruitmentsystem.address.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("""
            select a from address a
            right join Company c 
            on c.address.addressId = a.addressId
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
}
