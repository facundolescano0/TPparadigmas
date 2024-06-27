package org.example.controller;

import org.example.model.*;

public class CheckHipotecar {
    private Jugador jugador;
    private Barrio barrio;
    private Propiedad propiedad;

    public CheckHipotecar(Jugador jugador, Barrio barrio,Propiedad propiedad){
        this.jugador = jugador;
        this.barrio = barrio;
        this.propiedad = propiedad;
    }

    public boolean validarHipotecar(){
        if (!jugador.getPropiedades().contains(propiedad) || propiedad.getEstado() != EstadoPropiedades.COMPRADO){
            return false;

        }
        for (Propiedad propiedadDelBarrio: barrio.getPropiedades()){
            if (propiedadDelBarrio.getConstrucciones() != Construcciones.SIN_CASA){
                return false;
            }
        }
        return true;
    }
}
