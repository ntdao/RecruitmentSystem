package com.recruitmentsystem.address;

import com.recruitmentsystem.address.Province;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository {
    @Query("Select p from provinces p")
    List<Province> findAllProvinces();
}
