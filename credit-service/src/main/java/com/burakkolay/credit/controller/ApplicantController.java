package com.burakkolay.credit.controller;

import com.burakkolay.credit.config.RabbitMQConfig;
import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.DTO.ApplicantDTO;
import com.burakkolay.credit.model.entity.RegisterObject;
import com.burakkolay.credit.services.ApplicantService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/api/v1/applicant")
public class ApplicantController {


    private final ApplicantService applicantService;
    private final RabbitTemplate rabbitTemplate;



    public ApplicantController(ApplicantService applicantService, RabbitTemplate rabbitTemplate) {
        this.applicantService = applicantService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/all")
    public List<Applicant> getAllApplicants() {
        List<Applicant> allApplicants = applicantService.getAllApplicants();

        return allApplicants;
    }

    @GetMapping("/{id}")
    public ResponseEntity getApplicantById(@PathVariable("id") Long id) {
        Applicant byId = applicantService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(byId);
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
    public ResponseEntity deleteApplicantWithResponse(@PathVariable(name = "id") Long id) {
        applicantService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Related applicant deleted successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateApplicant(@RequestBody ApplicantDTO applicantDTO,@PathVariable(name = "id") Long id) {
        applicantService.update(applicantDTO,id);
        return ResponseEntity.status(HttpStatus.OK).body("Related applicant updated successfully");
    }

    /*******************************************************************************************************/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
        RedirectView redirectView = new RedirectView();
        if(applicantService.isApplicantExistsWithIdentificationNumber(applicantDTO.getIdentificationNumber()) || applicantService.isApplicantExistsWithPhoneNumber(applicantDTO.getPhoneNumber()) ){
            redirectView.setUrl("http://localhost:8080/api/v1/applicant/applicantAlreadyExists");
        }else{
            applicantService.create(applicantDTO);
            redirectView.setUrl("http://localhost:8080/");
        }
        return redirectView;
    }

    @GetMapping("/applicantAlreadyExists")
    public ModelAndView applicantAlreadyExists(){
        ModelAndView mav = new ModelAndView("exists3");
        return mav;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/deleteApplicant")
    public RedirectView deleteApplicant(@RequestParam Long applicantId){
        applicantService.delete(applicantId);
        return new RedirectView("http://localhost:8080/api/v1/applicant/showList");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long applicantId){
        ModelAndView mav = new ModelAndView("update-applicant-form");
        Applicant applicant = applicantService.getById(applicantId);
        mav.addObject("applicant",applicant);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/updateApplicant/{applicantId}")
    public RedirectView updateApplicant(@PathVariable("applicantId")Long applicantId,@ModelAttribute ApplicantDTO applicantDTO){
        applicantService.update(applicantDTO,applicantId);
        return new RedirectView("http://localhost:8080/api/v1/applicant/showList");
    }

    @PostMapping(value = {"/apply"})
    public RedirectView applyToCredit(@ModelAttribute RegisterObject registerObject){
        RedirectView redirectView = new RedirectView();
        if(applicantService.isApplicantExistsWithIdentificationNumber(registerObject.getIdentificationNumber())){
            Applicant applicant=applicantService.getByIdentificationNumber(registerObject.getIdentificationNumber());
            applicantService.applyCreditToApplicant(registerObject.getIdentificationNumber(), registerObject.getAssurance());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,applicant);
            redirectView.setUrl("http://localhost:8080/api/v1/credit/getCreditsByUser/"+registerObject.getIdentificationNumber());
        }else{
            redirectView.setUrl("http://localhost:8080/api/v1/applicant/applicantDoesNotExists");
        }
        return redirectView;
    }

    @GetMapping("/applicantDoesNotExists")
    public ModelAndView birtDateAndIdentificationNumberCheckFail() {
        ModelAndView mav = new ModelAndView("exists2");
        return mav;
    }

}
