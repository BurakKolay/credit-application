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
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final CreditService creditService;
    private final CreditMapper creditMapper;
    private final ApplicantMapper applicantMapper;

    private final int CREDIT_MULTIPLIER=4;

    @Autowired
    public ApplicantService(ApplicantRepository applicantRepository, CreditService creditService, CreditMapper creditMapper, ApplicantMapper applicantMapper) {
        this.applicantRepository = applicantRepository;
        this.creditService = creditService;
        this.creditMapper = creditMapper;
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

    public void applyToCredit(@RequestParam(name = "id") Long applicantId) {
            Credit credit = creditService.create();
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




}
