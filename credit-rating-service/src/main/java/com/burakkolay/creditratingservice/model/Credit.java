package com.burakkolay.creditratingservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "credit")
public class Credit {
    @Id
    private Long id;
    @Transient
    private int creditBalance;
    @Transient
    private String creditResult;


}
