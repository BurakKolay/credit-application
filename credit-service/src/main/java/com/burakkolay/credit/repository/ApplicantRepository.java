package com.burakkolay.credit.repository;

import com.burakkolay.credit.model.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant,Long> {

    // getting rid of the optional
    @Query(value = "SELECT id FROM applicant WHERE applicant.identification_number=:identificationNumber",nativeQuery = true)
    Long getApplicantByIdentificationNumber(@Param("identificationNumber") Long identificationNumber);

}
