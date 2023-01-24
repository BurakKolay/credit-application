package com.burakkolay.creditratingservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp creationDate;


}
