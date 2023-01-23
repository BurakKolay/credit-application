package com.burakkolay.credit.services;

import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.entity.Credit;
import com.burakkolay.credit.model.entity.CreditResult;
import org.springframework.stereotype.Service;

@Service
public class CreditResulterFacade {

    private static final int CREDIT_MULTIPlIER=4;

    private final TwilioService twilioService;

    public CreditResulterFacade(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    public Credit creditResulter(Credit credit, Applicant applicant){
        double salary = applicant.getMonthlyIncome();
        int creditRating = applicant.getCreditRating();

        // CreditRatingService is generating a random credit rating between 0-2000.
        // So %50 of the times it will be bigger than 1000.
        // That's why first if block is checking if the credit rating > 1000

        if (creditRating > 1000) {
            credit.setCreditResult(CreditResult.ACCEPTED);
            credit.setCreditBalance((salary * CREDIT_MULTIPlIER) + (credit.getAssurance() * 0.5));
        } else if (creditRating >= 500 && creditRating < 1000) {
            if (salary < 5000) {
                credit.setCreditResult(CreditResult.ACCEPTED);
                credit.setCreditBalance(10000 + (credit.getAssurance() * 0.1));
            } else if (salary >= 5000 && salary <= 10000) {
                credit.setCreditResult(CreditResult.ACCEPTED);
                credit.setCreditBalance(20000 + (credit.getAssurance() * 0.2));
            } else if (salary > 10000) {
                credit.setCreditResult(CreditResult.ACCEPTED);
                credit.setCreditBalance((salary * CREDIT_MULTIPlIER / 2) + (credit.getAssurance() * 0.25));
            }
        } else if (creditRating < 500) {
            credit.setCreditResult(CreditResult.DENIED);
        }

        twilioService.sendSMS(applicant,credit);
        return credit;
    }

}
