package com.burakkolay.credit.services;

import com.burakkolay.credit.model.DTO.ApplicantDTO;
import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.entity.Credit;
import com.burakkolay.credit.model.entity.CreditResult;
import com.burakkolay.credit.model.mapper.ApplicantMapper;
import com.burakkolay.credit.repository.ApplicantRepository;
import com.burakkolay.credit.repository.CreditRepository;
import com.twilio.rest.microvisor.v1.App;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicantServiceTest {


    @Mock
    private ApplicantRepository applicantRepository;

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private ApplicantService applicantService;
    @Test
    void getAllApplicants() {
        List<Applicant> expApplicantList = getSampleTestApplicants();

        when(applicantRepository.findAll()).thenReturn(expApplicantList);

        List<Applicant> actualApplicantList = applicantService.getAllApplicants();

        Assert.assertEquals(expApplicantList.size(),actualApplicantList.size());

        System.out.println("First: " + expApplicantList);
        for (int i = 0; i < expApplicantList.size(); i++) {
            Applicant currExpCustomer = expApplicantList.get(i);
            Applicant currActualCustomer = actualApplicantList.get(i);
            Assert.assertEquals(currExpCustomer.getIdentificationNumber(),currActualCustomer.getIdentificationNumber());
            Assert.assertEquals(currExpCustomer.getFirstName(),currActualCustomer.getFirstName());
        }
        System.out.println("Second: "+ expApplicantList);
    }

    @Test
    void getById() {
        Applicant expApplicant = getSampleTestApplicants().get(1);

        Optional<Applicant> opExpectedCustomer = Optional.of(expApplicant);

        when(applicantRepository.findById(Mockito.any())).thenReturn(opExpectedCustomer);

        Applicant actualApplicant = applicantService.getById(1L);

        Assert.assertEquals(actualApplicant.getIdentificationNumber(),expApplicant.getIdentificationNumber());
        Assert.assertEquals(actualApplicant.getFirstName(),expApplicant.getFirstName());
    }

    @Test
    void create() {
        Applicant applicant1 = getSampleTestApplicants().get(1);

        when(applicantRepository.save(applicant1)).thenReturn(applicant1);

        ApplicantDTO applicantDTO= new ApplicantDTO();

        applicantDTO.setIdentificationNumber(applicant1.getIdentificationNumber());
        applicantDTO.setFirstName(applicant1.getFirstName());
        applicantDTO.setLastName(applicant1.getLastName());
        applicantDTO.setPhoneNumber(applicant1.getPhoneNumber());
        applicantDTO.setMonthlyIncome(applicant1.getMonthlyIncome());
        applicantDTO.setDateOfBirth(applicant1.getDateOfBirth());
        Applicant applicant2 = applicantService.create(applicantDTO);

        Assert.assertEquals(applicant1.getFirstName(),applicant2.getFirstName());
    }

    @Test
    void delete() {
        Applicant applicant1 = getSampleTestApplicants().get(0);

        //when(applicantRepository.findById(Mockito.any())).thenReturn(Optional.of(applicant1));

        applicantService.delete(applicant1.getId());

        Mockito.verify(applicantRepository).deleteById(applicant1.getId());
    }

    @Test
    void addCreditToApplicant() {
        Applicant applicant = getSampleTestApplicants().get(0);
        Credit credit = getSampleTestCredits().get(0);
        Mockito.when(applicantRepository.save(applicant)).thenReturn(applicant);
        Mockito.when(creditRepository.save(credit)).thenReturn(credit);

        applicantService.addCreditToApplicant(credit,applicant.getIdentificationNumber());

    }

    private List<Applicant> getSampleTestApplicants(){
        List<Applicant> expApplicantList = new ArrayList<>();
        Applicant applicant1 = new Applicant(0L,24506231362L,"Burak","Kolay",1500,"+905369378309",750,null,null);
        Applicant applicant2 = new Applicant(1L,24506231363L,"Burak","Kolay",1500,"+905369378309",750,null,null);
        Applicant applicant3 = new Applicant(2L,24506231364L,"Burak","Kolay",1500,"+905369378309",750,null,null);
        expApplicantList.add(applicant1);
        expApplicantList.add(applicant2);
        expApplicantList.add(applicant3);
        return expApplicantList;
    }

    private List<Credit> getSampleTestCredits(){
        List<Credit> expCreditList = new ArrayList<>();
        Credit credit1 = new Credit(0L,1500, CreditResult.WAITING,1600,null);
        expCreditList.add(credit1);
        return expCreditList;
    }

}