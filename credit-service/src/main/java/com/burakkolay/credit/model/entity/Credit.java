package com.burakkolay.credit.model.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "credit")
public class Credit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double creditBalance;
    private CreditResult creditResult;
    private double assurance;
}
