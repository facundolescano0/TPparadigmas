package org.example.controller;

public class CheckStrToInt {
    protected int checkStringToInt(String str){
        try {
            return Integer.parseInt(str);
        }catch (NumberFormatException e){
            return Constantes.NEGATIVO;
        }
    }
}

