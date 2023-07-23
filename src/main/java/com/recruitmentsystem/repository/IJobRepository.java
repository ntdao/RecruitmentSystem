package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IJobRepository extends JpaRepository<Job, Integer>, PagingAndSortingRepository<Job, Integer> {
}
