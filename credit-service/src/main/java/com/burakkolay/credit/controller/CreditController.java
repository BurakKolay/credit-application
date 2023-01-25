package com.burakkolay.credit.controller;


import com.burakkolay.credit.model.DTO.CreditDTO;
import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.entity.Credit;
import com.burakkolay.credit.model.entity.TemporaryObject;
import com.burakkolay.credit.services.ApplicantService;
import com.burakkolay.credit.services.CreditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credit")
public class CreditController {

    private final CreditService creditService;

    private final ApplicantService applicantService;

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

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity deleteCredit2(@PathVariable(name = "id") Long id) {
//        //creditService.delete(id);
//        return ResponseEntity.status(HttpStatus.OK).body("Related credit deleted successfully");
//    }

    /*******************************************************************************************/

    @GetMapping("/showList")
    public ModelAndView showCreditList(){
        ModelAndView mav = new ModelAndView("list-credits");
        mav.addObject("credits",creditService.getAllCredits());
        return mav;
    }

    @RequestMapping(path = "/deleteCredit")
    public RedirectView deleteCredit(@RequestParam Long creditId){
        creditService.delete(creditId);
        RedirectView redirectView = new RedirectView("http://localhost:8080/api/v1/credit/showList");
        return redirectView;
    }

    @GetMapping("/getCreditByIdAndBirth")
    public RedirectView getCreditByIdAndBirth(@ModelAttribute TemporaryObject temporaryObject){
        RedirectView redirectView = new RedirectView();

        Applicant applicant = applicantService.getByIdentificationNumber(temporaryObject.getIdentificationNumber());
        Long value = temporaryObject.getDateOfBirth().getTime();
        Long value2 = applicant.getDateOfBirth().getTime();

        // There may be a change in the date of birth after coming to the credit rating service
        if(value.equals(value2)||value.equals(value2+75600000)){
            redirectView.setUrl("http://localhost:8080/api/v1/credit/getCreditsByUser/"+temporaryObject.getIdentificationNumber());
        }
        return redirectView;
    }

    @GetMapping("/getCreditsByUser/{id}")
    public ModelAndView getCreditByApplicant2(@PathVariable(name = "id") Long id){
        ModelAndView mav = new ModelAndView("list-credits-for-user");
        mav.addObject("credits",creditService.getCreditByApplicantId(id));
        return mav;
    }

}
