package com.burakkolay.credit.model.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applicant")
public class Applicant implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long identificationNumber;
    private String firstName;
    private String lastName;
    private double monthlyIncome;
    private String phoneNumber;

    private int creditRating;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonUnwrapped
    private List<Credit> credit;

}
