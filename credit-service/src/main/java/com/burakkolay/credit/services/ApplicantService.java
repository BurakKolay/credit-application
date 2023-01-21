package com.burakkolay.credit.services;


import com.burakkolay.credit.exception.ApplicantNotFoundException;
import com.burakkolay.credit.exception.EntityNotFoundException;
import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.entity.Credit;
import com.burakkolay.credit.model.DTO.ApplicantDTO;
import com.burakkolay.credit.model.entity.CreditResult;
import com.burakkolay.credit.model.mapper.ApplicantMapper;
import com.burakkolay.credit.repository.ApplicantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final CreditService creditService;
    private final ApplicantMapper applicantMapper;
    private final int CREDIT_MULTIPLIER=4;

    @Autowired
    public ApplicantService(ApplicantRepository applicantRepository, CreditService creditService, ApplicantMapper applicantMapper) {
        this.applicantRepository = applicantRepository;
        this.creditService = creditService;
        this.applicantMapper = applicantMapper;
    }


    public List<Applicant> getAllApplicants() {
        List<Applicant> allApplicants = applicantRepository.findAll();
        return allApplicants;
    }

    public Applicant getById(Long id) {
        Optional<Applicant> byId = applicantRepository.findById(id);
        return byId.orElseThrow(() -> {
            log.error("Applicant not found by id : " + id);
            return new EntityNotFoundException("Applicant", "id : " + id);
        });
    }

    public Applicant create(ApplicantDTO applicantDTO) {
        Applicant applicant = applicantMapper.toApplicant(applicantDTO);
        List<Credit> credits = new ArrayList<>();
        applicant.setCredit(credits);
        return applicantRepository.save(applicant);
    }

    public void delete(Long id) {
        getById(id);
        applicantRepository.deleteById(id);
    }

    public void addCreditToApplicant(Credit credit, Long applicantId) {
        Applicant byId = getById(applicantId);
        if (getById(applicantId)==null)
            throw new ApplicantNotFoundException("Applicant",applicantId);

           log.debug("Applicant credit : " + byId.getCredit());
            List<Credit> credits=byId.getCredit();
            credit.setCreditResult(CreditResult.WAITING);
            credits.add(credit);
            byId.setCredit(credits);

    }

    public Applicant update(ApplicantDTO applicantDTO,Long id){
            Applicant byId = getById(id);

            byId.setMonthlyIncome(applicantDTO.getMonthlyIncome());
            byId.setFirstName(applicantDTO.getFirstName());
            byId.setLastName(applicantDTO.getLastName());
            byId.setPhoneNumber(applicantDTO.getPhoneNumber());
            byId.setIdentificationNumber(applicantDTO.getIdentificationNumber());

        return applicantRepository.save(byId);
    }

    public Optional<Applicant> getByIdentificationNumber(Long identificationNumber){

        Long id = applicantRepository.getApplicantByIdentificationNumber(identificationNumber);
        Optional<Applicant> byIdentificationNumber=applicantRepository.findById(id);
        return Optional.ofNullable(byIdentificationNumber.orElseThrow(() -> {
            log.error("Applicant not found by identification number : " + id);
            return new ApplicantNotFoundException("Applicant", id);
        }));
    }
    /* TODO */
    public void applyToCredit(@RequestParam(name = "id") Long applicantId) {
            Credit credit = creditService.create();
            //int creditRatingEvaluation=creditService.gradeMap.floorEntry(applicant.getCreditRating().getCreditRating()).getValue();
        /*
            int creditRatingEvaluation=500;
            if(creditRatingEvaluation==0){ //0-499
                credit.setCreditResult("Credit Result : Declined");
                credit.setCreditBalance(0);
            }else if(creditRatingEvaluation==1 && applicant.getMonthlyIncome()<=5000){ // 500-999
                credit.setCreditResult("Credit Result : Approved");
                credit.setCreditBalance(10000);
            }else if(creditRatingEvaluation==1 && applicant.getMonthlyIncome()>5000){ // 500-999
                credit.setCreditResult("Credit Result : Approved");
                credit.setCreditBalance(20000);
            }else if(creditRatingEvaluation>1){ // 1000-+
                credit.setCreditResult("Credit Result : Approved");
                credit.setCreditBalance((int) (applicant.getMonthlyIncome()*CREDIT_MULTIPLIER));
            }

            // imaginary sms sent to customer here !
            creditService.sendSMS(applicant.getPhoneNumber());*/
             addCreditToApplicant(credit,applicantId);
    }
}
