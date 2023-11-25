package com.recruitmentsystem.address.address;

import com.recruitmentsystem.address.ward.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("""
            select a from address a
            right join Company c 
            on c.address.addressId = a.addressId
            where c.companyId = :id
            """)
    Address findByCompanyId(Integer id);

    @Query("""
            update address set address = :address,
            fullAddress = :fullAddress,
            ward.wardCode = :wardCode
            where addressId = :addressId
            """)
    void updateAddress(Integer addressId, String address, String fullAddress, String wardCode);

//    @Query("""
//            select new Address() from address a
//            right join  job_address ja on ja.addressId = a.addressId
//            right join Job j on j.jobId = ja.jobId
//            where j.jobId = :id
//            """)
//    List<Address> findByJobId(Integer id);

    @Query("""
            select a from address a
            right join User u
            on u.address.addressId = a.addressId
            """)
    Address findByUserId(Integer id);
}
