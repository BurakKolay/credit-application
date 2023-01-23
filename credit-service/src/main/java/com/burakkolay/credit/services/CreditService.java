package com.burakkolay.credit.services;

import com.burakkolay.credit.exception.EntityNotFoundException;
import com.burakkolay.credit.model.entity.Credit;
import com.burakkolay.credit.repository.CreditRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CreditService {
    private final CreditRepository creditRepository;


    @Autowired
    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }
    
    public Credit getById(Long id) {
        Optional<Credit> byId = creditRepository.findById(id);
        return byId.orElseThrow(() -> {
            log.error("Credit not found by id : " + id);
            return new EntityNotFoundException("Credit", "id : " + id);
        });
    }

    public Credit create() {
        Credit credit = new Credit();
        return creditRepository.save(credit);
    }

    public void delete(Long id) {
        getById(id);
        creditRepository.deleteById(id);
    }

}
