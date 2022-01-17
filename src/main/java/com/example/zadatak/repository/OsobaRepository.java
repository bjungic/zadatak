package com.example.zadatak.repository;

import com.example.zadatak.model.Osoba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OsobaRepository extends JpaRepository<Osoba, Long> {

    Optional<Osoba> findByOIB(String OIB);

}
