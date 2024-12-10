package com.example.ohjelmistotuotanto1;


public record PalveluEsim(int id, String nimi, double hinta, String kuvaus) {



    @Override
    public String toString() {
        return nimi;
    }

}
