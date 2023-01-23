package com.burakkolay.creditratingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "credit")
public class Credit {
    @Id
    private Long id;
    @Transient
    private double creditBalance;
    @Transient
    private String creditResult;
    @Transient
    private double assurance;


}
