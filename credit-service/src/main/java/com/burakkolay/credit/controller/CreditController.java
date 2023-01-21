package com.burakkolay.credit.controller;


import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.entity.Credit;
import com.burakkolay.credit.model.DTO.CreditDTO;
import com.burakkolay.credit.model.mapper.CreditMapper;
import com.burakkolay.credit.repository.ApplicantRepository;
import com.burakkolay.credit.services.ApplicantService;
import com.burakkolay.credit.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/credit")
public class CreditController {

    private CreditService creditService;

    private ApplicantService applicantService;


    public CreditController(CreditService creditService, ApplicantService applicantService) {
        this.creditService = creditService;
        this.applicantService = applicantService;
    }

    @GetMapping("/all")
    public ResponseEntity getAllCredits(){
        List<Credit> allCredits = creditService.getAllCredits();

        return ResponseEntity.ok(allCredits);
    }

    @GetMapping("/id")
    public ResponseEntity getCreditById(@PathVariable("id") Long id){
        Credit credit = creditService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(credit);
    }

    @GetMapping("/{in}")
    public ResponseEntity getCreditByIdentificationNumber(@PathVariable("in") Long identificationNumber){
        Credit byId = creditService.getById(identificationNumber);
        return ResponseEntity.status(HttpStatus.OK).body(byId);

    }

    @PostMapping("/create/{id}")
    public ResponseEntity createCredit(@RequestBody CreditDTO creditDTO, @PathVariable("id") Long id){
       Applicant applicant =  applicantService.setCreditToApplicant(id, creditDTO);
        if(applicant.getCredit()==null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Credit could not be created successfully");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(applicant.getCredit().get(applicant.getCredit().size()-1));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCredit(@PathVariable(name = "id") Long id) {
        creditService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Related credit deleted successfully");
    }
}
