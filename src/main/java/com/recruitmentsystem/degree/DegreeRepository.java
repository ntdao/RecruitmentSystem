package com.recruitmentsystem.degree;

import com.recruitmentsystem.degree.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Integer> {
    List<Degree> findByDegreeNameVIContains(String name);

    Optional<Degree> findByDegreeName(String name);
}
