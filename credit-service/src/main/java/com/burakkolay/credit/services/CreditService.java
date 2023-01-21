package com.burakkolay.credit.services;

import com.burakkolay.credit.exception.EntityNotFoundException;
import com.burakkolay.credit.interfaces.SMSInterface;
import com.burakkolay.credit.model.entity.Credit;
import com.burakkolay.credit.repository.CreditRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Slf4j
@Service
public class CreditService implements SMSInterface {

    public static TreeMap<Integer, Integer> gradeMap = new TreeMap<>();
    static {
        gradeMap.put(0,0); //0-499
        gradeMap.put(500,1); //500-999
        gradeMap.put(1000,2); //1000-1899
        gradeMap.put(1900,3); //1900-+
    }

    private final CreditRepository creditRepository;


    @Autowired
    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public List<Credit> getAllCredits() {
        List<Credit> allCredits = creditRepository.findAll();
        return allCredits;
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

    @Override
    public String sendSMS(String phoneNumber) {
        return "Credit result SMS sent to registered phone number: "+phoneNumber;
    }
}
