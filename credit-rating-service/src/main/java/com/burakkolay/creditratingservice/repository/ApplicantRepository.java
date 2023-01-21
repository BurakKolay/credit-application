package com.burakkolay.creditratingservice.repository;

import com.burakkolay.creditratingservice.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant,Long> {

}
