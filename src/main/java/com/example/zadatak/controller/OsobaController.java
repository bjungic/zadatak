package com.example.zadatak.controller;

import com.example.zadatak.data.OsobaDto;
import com.example.zadatak.service.OsobaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/osoba")
public class OsobaController {

    private final OsobaService osobaService;

    public OsobaController(OsobaService osobaService) {
        this.osobaService = osobaService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getOsoba(@RequestParam("oib") String oib) {
        return new ResponseEntity(osobaService.getOsoba(oib), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveOsoba(@RequestBody() OsobaDto osobaDto) {
        osobaService.saveOsoba(osobaDto);
        return new ResponseEntity("Osoba sa oib: " + osobaDto.getOIB() + " spremljena.", HttpStatus.OK);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteOsoba(@RequestParam("oib") String oib) {
        osobaService.deleteOsoba(oib);
        return new ResponseEntity("Osoba sa oib: " + oib + " obrisana.", HttpStatus.OK);
    }
}
