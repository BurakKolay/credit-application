package com.burakkolay.credit.controller;

import com.burakkolay.credit.config.RabbitMQConfig;
import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.DTO.ApplicantDTO;
import com.burakkolay.credit.model.entity.RegisterObject;
import com.burakkolay.credit.model.entity.TemporaryObject;
import com.burakkolay.credit.repository.ApplicantRepository;
import com.burakkolay.credit.services.ApplicantService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
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

//    @GetMapping("/byIn/{in}")
//    public ResponseEntity getApplicantByIdentificationNumber(@PathVariable("in") Long identificationNumber) {
//        Optional<Applicant> byIn = applicantService.getByIdentificationNumber(identificationNumber);
//        return ResponseEntity.status(HttpStatus.OK).body(byIn);
//    }

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
    public ResponseEntity deleteApplicantWithResponse(@PathVariable(name = "id") Long id) {
        applicantService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Related applicant deleted successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateApplicant(@RequestBody ApplicantDTO applicantDTO,@PathVariable(name = "id") Long id) {
        applicantService.update(applicantDTO,id);
        return ResponseEntity.status(HttpStatus.OK).body("Related applicant updated successfully");
    }


//    @PutMapping(value = {"/apply/{id}/{assurance}","/apply/{id}"})
//    public ResponseEntity applyToCredit2(@PathVariable(name = "id") Long applicantId,@PathVariable(name = "assurance",required = false) Double assurance){
//
//        Applicant applicant=applicantService.getById(applicantId);
//        applicantService.applyToCredit(applicantId,assurance);
//        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,applicant);
//        return applicantService.creditResultResponse(applicant);
//    }
    /*******************************************************************************************************/

    @GetMapping("/showList")
    public ModelAndView showApplicantList(){
        ModelAndView mav = new ModelAndView("list-applicants");
        mav.addObject("applicants",applicantService.getAllApplicants());
        return mav;
    }

    @GetMapping("/addApplicantForm")
    public ModelAndView addNewApplicantForm(){
        ModelAndView mav = new ModelAndView("add-applicant-form");
        Applicant applicant = new Applicant();
        mav.addObject("applicant",applicant);
        return mav;
    }

    @PostMapping("/saveApplicant")
    public RedirectView saveApplicant(@ModelAttribute ApplicantDTO applicantDTO){
        applicantService.create(applicantDTO);
        RedirectView redirectView = new RedirectView("http://localhost:8080/api/v1/applicant/showList");
        return redirectView;
    }

    @RequestMapping(path = "/deleteApplicant")
    public RedirectView deleteApplicant(@RequestParam Long applicantId){
        applicantService.delete(applicantId);
        RedirectView redirectView = new RedirectView("http://localhost:8080/api/v1/applicant/showList");
        return redirectView;
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long applicantId){
        ModelAndView mav = new ModelAndView("update-applicant-form");
        Applicant applicant = applicantService.getById(applicantId);
        mav.addObject("applicant",applicant);
        return mav;
    }

    @RequestMapping(path = "/updateApplicant/{applicantId}")
    public RedirectView updateOwner(@PathVariable("applicantId")Long applicantId,@ModelAttribute ApplicantDTO applicantDTO){
        applicantService.update(applicantDTO,applicantId);
        RedirectView redirectView = new RedirectView("http://localhost:8080/api/v1/applicant/showList");
        return redirectView;
    }

    @PostMapping(value = {"/apply"})
    public RedirectView applyToCredit(@ModelAttribute RegisterObject registerObject){

        Applicant applicant=applicantService.getByIdentificationNumber(registerObject.getIdentificationNumber());
        applicantService.applyCreditToApplicant(registerObject.getIdentificationNumber(), registerObject.getAssurance());
        //applicantService.applyToCredit(applicantIdNumber,assurance);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,applicant);
        //applicantService.creditResultResponse(applicant);
        RedirectView redirectView = new RedirectView("http://localhost:8080/api/v1/credit/showList");
        return redirectView;
    }

    @GetMapping("/addCreditForm")
    public ModelAndView showAddCreditForm(){
        ModelAndView mav = new ModelAndView("add-credit-form");
        RegisterObject registerObject = new RegisterObject();
        mav.addObject("dummy",registerObject);
        return mav;
    }

    @GetMapping("/showCreditForm")
    public ModelAndView showCreditForm(){
        ModelAndView mav = new ModelAndView("find-credit");
        TemporaryObject applicant = new TemporaryObject();
        mav.addObject("applicant",applicant);
        return mav;
    }
}
