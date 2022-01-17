package com.example.zadatak.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "osobe")
public class Osoba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ime")
    private String ime;

    @Column(name = "prezime")
    private String prezime;

    @Column(name = "oib")
    private String OIB;

    @Column(name = "status")
    private Boolean status;
}
