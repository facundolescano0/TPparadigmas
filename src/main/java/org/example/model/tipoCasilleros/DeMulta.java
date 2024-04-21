package org.example.model.tipoCasilleros;

import org.example.model.Jugador;

public class DeMulta extends Casillero implements CasilleroEjecutable {
    private int montoMulta;
    public DeMulta(int ubicacion,int montoMulta){
        super("Multa",TipoCasillero.MULTA,ubicacion);
        this.montoMulta = montoMulta;
    }


    public void ejecutarCasillero(Jugador jugador) {
        jugador.restarPlata(montoMulta);
        System.out.println(jugador.getNombre() + " pagaste multa de $"+montoMulta);
    }
}
