package com.burakkolay.creditratingservice.repository;

import com.burakkolay.creditratingservice.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit,Long> {
}
