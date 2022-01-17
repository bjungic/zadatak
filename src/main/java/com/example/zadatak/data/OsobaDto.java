package com.example.zadatak.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OsobaDto {

    @JsonProperty(value = "ime")
    private String ime;

    @JsonProperty(value = "prezime")
    private String prezime;

    @JsonProperty(value = "OIB")
    private String OIB;

    @JsonProperty(value = "status")
    private Boolean status;
}
