package org.example.model.tipoCasilleros;

import org.example.model.Jugador;

public class LlegadaPartida extends Casillero {
    private double bono;

    public LlegadaPartida(int ubicacion,double bono) {
        super("Llegada/Partida",TipoCasillero.LLEGADA_INICIO,ubicacion);
        this.bono = bono;
    }
}