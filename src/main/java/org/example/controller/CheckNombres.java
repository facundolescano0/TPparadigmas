package org.example.controller;

public class CheckNombres {
    public boolean checkNombres(String nombre) {
        String[] nombres = nombre.split(" ");
        return nombres.length >= 2 && nombres.length <= 4;
    }
}
