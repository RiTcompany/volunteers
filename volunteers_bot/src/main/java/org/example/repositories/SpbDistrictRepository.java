package org.example.repositories;

import org.example.entities.SpbDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpbDistrictRepository extends JpaRepository<SpbDistrict, Integer> {
    boolean existsByName(String name);
}
