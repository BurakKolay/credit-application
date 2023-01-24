package com.burakkolay.creditratingservice.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applicant")
public class Applicant implements Serializable {

    @Transient
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    private Long identificationNumber;
    @Transient
    private String firstName;
    @Transient
    private String lastName;
    @Transient
    private double monthlyIncome;
    @Transient
    private String phoneNumber;

    private int creditRating;
    @Transient
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "applicant_id")
    private List<Credit> credit;

    public Applicant deepCopy(Applicant applicant){

        return new Applicant(applicant.getId(),
                applicant.getIdentificationNumber(),
                applicant.getFirstName(),
                applicant.getLastName(),
                applicant.getMonthlyIncome(),
                applicant.getPhoneNumber(),
                applicant.getCreditRating(),
                applicant.getDateOfBirth(),
                applicant.getCredit());
    }


}