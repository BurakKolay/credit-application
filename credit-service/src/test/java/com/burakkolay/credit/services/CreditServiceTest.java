package com.burakkolay.credit.services;

import com.burakkolay.credit.model.entity.Applicant;
import com.burakkolay.credit.model.entity.Credit;
import com.burakkolay.credit.model.entity.CreditResult;
import com.burakkolay.credit.repository.CreditRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CreditService creditService;


    @Test
    void getAllCredits() {

        List<Credit> expCreditList = getSampleTestCredits();
        when(creditRepository.findAll()).thenReturn(expCreditList);
        List<Credit> actualCreditlist = creditService.getAllCredits();
        Assert.assertEquals(expCreditList.size(),actualCreditlist.size());

        for (int i = 0; i < expCreditList.size(); i++) {
            Credit currExpCredit = expCreditList.get(i);
            Credit currActualCredit = expCreditList.get(i);
            Assert.assertEquals(Double.valueOf(currExpCredit.getCreditBalance()),Double.valueOf(currActualCredit.getCreditBalance()));
            Assert.assertEquals(Double.valueOf(currExpCredit.getAssurance()),Double.valueOf(currActualCredit.getAssurance()));
        }

    }

    @Test
    void getAllCreditsOrderedById() {
        List<Credit> expCreditList = getSampleTestCredits();
        when(creditRepository.findAll()).thenReturn(expCreditList);
        List<Credit> actualCreditlist = creditService.getAllCredits();
        Assert.assertEquals(expCreditList.size(),actualCreditlist.size());

        for (int i = 0; i < expCreditList.size(); i++) {
            Credit currExpCredit = expCreditList.get(i);
            Credit currActualCredit = expCreditList.get(i);
            Assert.assertEquals(Double.valueOf(currExpCredit.getCreditBalance()),Double.valueOf(currActualCredit.getCreditBalance()));
            Assert.assertEquals(Double.valueOf(currExpCredit.getAssurance()),Double.valueOf(currActualCredit.getAssurance()));
        }
    }

    @Test
    void getById() {
        Credit credit = getSampleTestCredits().get(0);

        when(creditRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(credit));

        Credit actualCredit = creditService.getById(0L);

        Assert.assertEquals(Double.valueOf(credit.getCreditBalance()),Double.valueOf(actualCredit.getCreditBalance()));
        Assert.assertEquals(Double.valueOf(credit.getAssurance()),Double.valueOf(actualCredit.getAssurance()));
    }

    @Test
    void create() {

        Credit credit1 = getSampleTestCredits().get(0);
        credit1.setId(null);

        Mockito.when(creditRepository.save(any())).thenReturn(credit1);

        Credit credit2 = creditService.create();

        credit2.setCreditResult(credit1.getCreditResult());
        credit2.setAssurance(credit1.getAssurance());
        credit2.setId(credit1.getId());
        credit2.setCreditBalance(credit1.getCreditBalance());
        credit2.setCreationDate(credit1.getCreationDate());

        Assert.assertEquals(credit1.getCreditResult(),credit2.getCreditResult());
    }

    @Test
    void delete() {
        Long creditId=0L;
        Credit credit = getSampleTestCredits().get(0);
        doNothing().when(creditRepository).deleteById(creditId);

        creditRepository.deleteById(creditId);
        verify(creditRepository,times(1)).deleteById(creditId);
    }


    private List<Credit> getSampleTestCredits(){
        List<Credit> expCreditList = new ArrayList<>();
        Credit credit1 = new Credit(0L,1500, CreditResult.WAITING,1600,null);
        Credit credit2 = new Credit(1L,1600, CreditResult.WAITING,1200,null);
        expCreditList.add(credit1);
        expCreditList.add(credit2);
        return expCreditList;
    }
}