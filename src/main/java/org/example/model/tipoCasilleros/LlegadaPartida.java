package org.example.model.tipoCasilleros;



public class LlegadaPartida extends Casillero {
    private double bono;

    public LlegadaPartida(int ubicacion,double bono) {
        super("Llegada/Partida",TipoCasillero.LLEGADA_INICIO,ubicacion,false,false);
        this.bono = bono;
    }
}