package org.example.repositories;

import org.example.entities.EducationInstitution;
import org.example.enums.EEducationInstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationInstitutionRepository extends JpaRepository<EducationInstitution, Integer> {
    List<EducationInstitution> findAllByType(EEducationInstitution educationInstitution);

    boolean existsByShortNameAndType(String name, EEducationInstitution type);
}
