package org.example.controller;

public class CheckNum {
    public boolean checkNumeros(String cantidad, ConfiguracionCheckArgumentos config) {
        CheckStrToInt checkStrToInt = new CheckStrToInt();
        int numeroPuesto = checkStrToInt.checkStringToInt(cantidad);
        if ((config == ConfiguracionCheckArgumentos.DINERO_INICIAL && numeroPuesto < Constantes.CANTIDAD_INICIAL_MINIMA) || (config == ConfiguracionCheckArgumentos.CASILLEROS && numeroPuesto < Constantes.CANTIDAD_CASILLEROS_MINIMA)|| numeroPuesto <= 0){
            return false;
        }else{
            return true;
        }
    }
}
