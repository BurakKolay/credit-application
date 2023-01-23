package com.burakkolay.credit.repository;

import com.burakkolay.credit.model.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant,Long> {


    @Query(value = "SELECT id FROM applicant WHERE applicant.identification_number=:identificationNumber",nativeQuery = true)
    Long getApplicantByIdentificationNumber(@Param("identificationNumber") Long identificationNumber);


//    @Query(value = "SELECT COUNT(PRODUCT_CATEGORY) FROM PRODUCT  WHERE USER_ID= {SELECT ID FROM USERS WHERE USER_NAME= (:username)} AND PRODUCT_CATEGORY = :categoryNumber",nativeQuery = true)
//    Integer findProductCategoryByUsername(@Param("categoryNumber")int categoryNumber,@Param("username")String username);

    @Query(value = "SELECT * FROM applicant WHERE id= (SELECT applicant_id FROM applicant_credit WHERE credit_id= (:creditId))",nativeQuery = true)
    Applicant getApplicantFromCreditId(@Param("creditId")Long creditId);
}
