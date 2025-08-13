package com.skillforge.skillforge_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "educations")
@AllArgsConstructor
public class Education {


    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String degree;
    private String institution;
    private String major;
    private int year;

    public Education() {

    }
}
