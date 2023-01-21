package com.burakkolay.credit.controller;

import com.burakkolay.credit.config.RabbitMQConfig;
import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.DTO.ApplicantDTO;
import com.burakkolay.credit.model.entity.Credit;
import com.burakkolay.credit.model.entity.CreditResult;
import com.burakkolay.credit.repository.ApplicantRepository;
import com.burakkolay.credit.services.ApplicantService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/applicant")
public class ApplicantController {


    private ApplicantService applicantService;
    private RabbitTemplate rabbitTemplate;
    private final ApplicantRepository applicantRepository;

    public ApplicantController(ApplicantService applicantService, RabbitTemplate rabbitTemplate,
                               ApplicantRepository applicantRepository) {
        this.applicantService = applicantService;
        this.rabbitTemplate = rabbitTemplate;
        this.applicantRepository = applicantRepository;
    }

    @GetMapping("/all")
    public ResponseEntity getAllApplicants() {
        List<Applicant> allApplicants = applicantService.getAllApplicants();

        return ResponseEntity.ok(allApplicants);
    }

    @GetMapping("/{id}")
    public ResponseEntity getApplicantById(@PathVariable("id") Long id) {
        Applicant byId = applicantService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(byId);
    }

    @GetMapping("/byIn/{in}")
    public ResponseEntity getApplicantByIdentificationNumber(@PathVariable("in") Long identificationNumber) {
        Optional<Applicant> byIn = applicantService.getByIdentificationNumber(identificationNumber);
        return ResponseEntity.status(HttpStatus.OK).body(byIn);
    }

    @PostMapping("/create")
    public ResponseEntity createNewApplicant(@RequestBody ApplicantDTO applicantDTO) {
        Applicant applicant = applicantService.create(applicantDTO);

        if (applicant == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Applicant could not be created successfully");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(applicant);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteApplicant(@PathVariable(name = "id") Long id) {
        applicantService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Related applicant deleted successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateApplicant(@RequestBody ApplicantDTO applicantDTO,@PathVariable(name = "id") Long id) {
        applicantService.update(applicantDTO,id);
        return ResponseEntity.status(HttpStatus.OK).body("Related applicant updated successfully");
    }

    //@RabbitListener(queues = "credit-queue-2")
    @PutMapping("/apply/{id}")
    public ResponseEntity applyToCredit(@PathVariable(name = "id") Long applicantId){

        //applicantService.applyToCredit(applicantId);
        Applicant applicant=applicantService.getById(applicantId);
        applicantService.applyToCredit(applicantId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,applicant);



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
             return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(CreditResult.WAITING);
         }
    }

    @Async
    @RabbitListener(queues = "credit-queue-2")
    public void listener(Applicant applicant){
        //ystem.out.println(applicant);

        applicantRepository.save(applicant);
        /*
        Credit credit = applicantService.get;
        credit= (Credit) applicant.getCredit();
        if(applicant.getCreditRating()==0){ //0-499
            credit.setCreditResult("Credit Result : Declined");
            credit.setCreditBalance(0);
        }else if(applicant.getCreditRating()==500 && applicant.getMonthlyIncome()<=5000){ // 500-999
            credit.setCreditResult("Credit Result : Declined");
            credit.setCreditBalance(10000);
        }else if(applicant.getCreditRating()==750 && applicant.getMonthlyIncome()>5000){ // 500-999
            credit.setCreditResult("Credit Result : Approved");
            credit.setCreditBalance(20000);
        }else if(applicant.getCreditRating()>750){ // 1000-+
            credit.setCreditResult("Credit Result : Approved");
            //credit.setCreditBalance((int) (applicant.getMonthlyIncome()*CREDIT_MULTIPLIER));
        }*/
        System.out.println(applicant);
    }


}
