package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
}
