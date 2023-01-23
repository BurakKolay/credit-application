package com.burakkolay.credit.repository;


import com.burakkolay.credit.model.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit,Long> {

    @Query(value = "SELECT * FROM credit WHERE credit_result=1",nativeQuery = true)
    List<Credit> getAllCreditWaiting();

}
