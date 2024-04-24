package org.example.controller;

import org.example.model.Propiedad;
import org.example.model.Barrio;
import org.example.model.Jugador;
import org.example.model.Construcciones;
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
        if (!jugador.getPropiedades().contains(propiedad)){
            System.out.println("ERROR: EL JUGADOR "+ jugador.getNombre() + " NO ES DUEÑO DE LA PROPIEDAD");
            return false;
        }
        for (Propiedad propiedadDelBarrio: barrio.getPropiedades()){
            if (propiedadDelBarrio.getConstrucciones() != Construcciones.SIN_CASA){
                System.out.println("ERROR: HAY CONSTURCCIONES EN LAS CASAS DEL BARRIO. TODAS TIENEN QUE ESTAR SIN CASA");
                return false;
            }
        }
        return true;
    }

}
