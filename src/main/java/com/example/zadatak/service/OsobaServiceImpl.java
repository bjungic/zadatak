package com.example.zadatak.service;

import com.example.zadatak.data.OsobaDto;
import com.example.zadatak.model.Osoba;
import com.example.zadatak.repository.OsobaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Service
public class OsobaServiceImpl implements OsobaService {

    private String folder = "osoba" + System.getProperty("file.separator");
    private String delimiter = ";";

    private final OsobaRepository osobaRepository;

    public OsobaServiceImpl(OsobaRepository osobaRepository) {
        this.osobaRepository = osobaRepository;
    }

    @Override
    public void saveOsoba(OsobaDto osobaDto) {

        //PROVJERI POSTOJI LI OSOBA U BAZI
        if (osobaRepository.findByOIB(osobaDto.getOIB()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Već postoji osoba s oib: " + osobaDto.getOIB());

        Osoba osoba = new Osoba();
        osoba.setIme(osobaDto.getIme());
        osoba.setPrezime(osobaDto.getPrezime());
        osoba.setOIB(osobaDto.getOIB());
        osoba.setStatus(osobaDto.getStatus());

        osobaRepository.save(osoba);
    }

    @Override
    public String getOsoba(String OIB) {

        //DOHVATI OSOBU IZ BAZE
        Osoba osoba = osobaRepository.findByOIB(OIB).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ne postoji osoba sa OIB: " + OIB));

        //ZAPISE FILE U FOLDER
        createfile(osoba);

        OsobaDto osobaRequest = new OsobaDto();
        osobaRequest.setIme(osoba.getIme());
        osobaRequest.setPrezime(osoba.getPrezime());
        osobaRequest.setOIB(osoba.getOIB());
        osobaRequest.setStatus(osoba.getStatus());

        String odgovor = "{\"ime\": \"" + osoba.getIme() + "\"," +
                "\"prezime\": \"" + osoba.getPrezime() + "\"," +
                "\"OIB\": \"" + osoba.getOIB() + "\"," +
                "\"status\": " + osoba.getStatus() +
                "}";

        return odgovor;
    }

    @Override
    public void deleteOsoba(String oib) {
        //DELETE OSOBA IZ BAZE
        Osoba osoba = osobaRepository.findByOIB(oib).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ne postoji osoba sa oib: " + oib));
        osobaRepository.delete(osoba);

        //PROMIJENI STATUS U FILE-U
        checkFile(osoba);
    }

    private void createfile(Osoba osoba) {
        try {
            //STVORI FOLDER AKO NE POSTOJI
            File osobaFolder = new File(folder);
            if (!osobaFolder.exists())
                osobaFolder.mkdir();

            checkFile(osoba);

            String string = osoba.getIme() + delimiter + osoba.getPrezime() + delimiter + osoba.getOIB() + delimiter + osoba.getStatus();
            FileWriter fileWriter = new FileWriter(folder + osoba.getOIB() + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + ".txt");
            fileWriter.write(string);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(Osoba osoba) {
        File folderOsoba = new File(folder);
        File[] listOfFiles = folderOsoba.listFiles();

        //PROLAZI KROZ SVE FILE-OVE U FOLDERU I USPOREDUJE S OIB-OM
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (osoba.getOIB().equals(file.getName().substring(0, file.getName().indexOf("_")))) {
                    try {
                        Scanner scanner = new Scanner(file);
                        String s = scanner.nextLine();
                        s = s.substring(0, s.lastIndexOf(delimiter) + 1) + "false";
                        scanner.close();
                        String fileName = file.getAbsolutePath();
                        file.delete();
                        FileWriter fileWriter = new FileWriter(fileName);
                        fileWriter.write(s);
                        fileWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
