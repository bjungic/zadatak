package com.example.zadatak.service;

import com.example.zadatak.data.OsobaDto;

public interface OsobaService {

    void saveOsoba(OsobaDto osobaDto);

    String getOsoba(String OIB);

    void deleteOsoba(String oib);
}
