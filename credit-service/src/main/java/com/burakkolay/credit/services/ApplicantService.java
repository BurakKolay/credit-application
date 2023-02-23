package com.burakkolay.credit.services;


import com.burakkolay.credit.exception.ApplicantNotFoundException;
import com.burakkolay.credit.exception.EntityNotFoundException;
import com.burakkolay.credit.model.DTO.CreditDTO;
import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.entity.Credit;
import com.burakkolay.credit.model.DTO.ApplicantDTO;
import com.burakkolay.credit.model.entity.CreditResult;
import com.burakkolay.credit.model.mapper.ApplicantMapper;
import com.burakkolay.credit.model.mapper.CreditMapper;
import com.burakkolay.credit.repository.ApplicantRepository;
import com.burakkolay.credit.repository.CreditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@EnableScheduling
@EnableAsync
public class ApplicantService {
    private final CreditRepository creditRepository;
    private final ApplicantRepository applicantRepository;
    private final CreditService creditService;
    private final CreditMapper creditMapper;
    private final ApplicantMapper applicantMapper;
    private final CreditResulterFacade creditResulterFacade;


    public ApplicantService(CreditRepository creditRepository, ApplicantRepository applicantRepository, CreditService creditService, CreditMapper creditMapper, ApplicantMapper applicantMapper, CreditResulterFacade creditResulterFacade) {
        this.creditRepository = creditRepository;
        this.applicantRepository = applicantRepository;
        this.creditService = creditService;
        this.creditMapper = creditMapper;
        this.applicantMapper = applicantMapper;
        this.creditResulterFacade = creditResulterFacade;
    }

    public List<Applicant> getAllApplicants() {
        return applicantRepository.findAll();
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
        //getById(id);
        applicantRepository.deleteById(id);
    }

    public void addCreditToApplicant(Credit credit, Long applicantId) {
        Applicant byId = getByIdentificationNumber(applicantId);
        if (getByIdentificationNumber(applicantId)==null)
            throw new ApplicantNotFoundException("Applicant",applicantId);

        log.debug("Applicant credit : " + byId.getCredit());
        List<Credit> credits=byId.getCredit();
        credit.setCreditResult(CreditResult.WAITING);
        credits.add(credit);
        byId.setCredit(credits);
        creditRepository.save(credit);
        System.out.println(byId);
    }

    public boolean isApplicantExistsWithIdentificationNumber(Long identificationNumber){
        return applicantRepository.existsApplicantByIdentificationNumber(identificationNumber);
    }

    public boolean isApplicantExistsWithPhoneNumber(String phoneNumber){
        return applicantRepository.existsApplicantByPhoneNumber(phoneNumber);
    }

    public void applyCreditToApplicant(Long id,double assurance){
        Applicant applicant = getByIdentificationNumber(id);
        Credit credit = creditService.create();
        //if(assurance==null) assurance=0.0;
        credit.setAssurance(assurance);
        addCreditToApplicant(credit,id);
    }

    public void update(ApplicantDTO applicantDTO, Long id){
            Applicant byId = getById(id);

            byId.setMonthlyIncome(applicantDTO.getMonthlyIncome());
            byId.setFirstName(applicantDTO.getFirstName());
            byId.setLastName(applicantDTO.getLastName());
            byId.setPhoneNumber(applicantDTO.getPhoneNumber());

        applicantRepository.save(byId);
    }

    public Applicant getByIdentificationNumber(Long identificationNumber){

        Applicant applicant = applicantRepository.getApplicantByIdentificationNumber(identificationNumber);
        return applicant;
    }

    public void applyToCredit(Long applicantId,Double assurance) {
            Credit credit = creditService.create();
            if(assurance==null) assurance=0.0;
            credit.setAssurance(assurance);
            addCreditToApplicant(credit,applicantId);
    }


    @RabbitListener(queues = "credit-queue-2")
    public void listener(Applicant applicant){
        applicantRepository.save(applicant);
    }

    public ResponseEntity creditResultResponse(Applicant applicant){
        if (Objects.equals(applicant.getCredit().get(applicant.getCredit().size()-1).getCreditResult(), CreditResult.ACCEPTED)) {
            System.out.println("Applied to credit success");
            return ResponseEntity.status(HttpStatus.OK)
                    .body("""
                            Applied to credit successfully.
                            """+applicant.getCredit().get(applicant.getCredit().size()-1));
        } else if(Objects.equals(applicant.getCredit().get(applicant.getCredit().size()-1).getCreditResult(), CreditResult.DENIED)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("""
                            Applied to credit successfully.
                            """+applicant.getCredit().get(applicant.getCredit().size()-1));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(CreditResult.WAITING);
        }
    }

    public Applicant setCreditToApplicant(Long id,CreditDTO creditDTO){
        Applicant applicant = getById(id);
        Credit credit = creditMapper.toCredit(creditDTO);
        List<Credit> credits=applicant.getCredit();
        credits.add(credit);
        applicant.setCredit(credits);
       return applicantRepository.save(applicant);
    }

    @Scheduled(cron = "*/5 * * * * *")
    @Async
    public void creditResult(){
        List<Credit> allCreditWaiting = creditRepository.getAllCreditWaiting();
        for (Credit credit:allCreditWaiting) {
            Applicant applicantFromCreditId = applicantRepository.getApplicantFromCreditId(credit.getId());
            creditRepository.save(creditResulterFacade.creditResulter(credit,applicantFromCreditId));
        }
    }

}
