package org.example.controller;

import org.example.model.*;

import java.util.ArrayList;

public class CheckGanarJugador {
    private Tablero tablero;

    CheckGanarJugador(Tablero tablero) {
        this.tablero = tablero;
    }

    public boolean ComprobarGanarJugador(Jugador jugador) {
        ArrayList<Barrio> barrios = tablero.getBarrios();
        ArrayList<Propiedad> propiedadesJugador = jugador.getPropiedades();
        for (Barrio barrio : barrios) {
            ArrayList<Propiedad> propiedadesBarrio = barrio.getPropiedades();
            boolean todasPropiedadesPertenecenAJugador = propiedadesJugador.containsAll(propiedadesBarrio);
            if (todasPropiedadesPertenecenAJugador) {
                boolean todasPropiedadesEnHotel = true;
                for (Propiedad propiedad : propiedadesBarrio) {
                    if (propiedad.getConstrucciones() != Construcciones.HOTEL) {
                        todasPropiedadesEnHotel = false;
                        break;
                    }
                }
                if (todasPropiedadesEnHotel) {
            //        System.out.println("\t\t¡¡FELICITACIONES!!\nEl jugador "+ jugador.getNombre() + "ha completado todo un barrio con hoteles. Por eso, HA GANADO");
                    jugador.setEstado(Estado.Gano);
                    return true;
                }
            }
        }
        return false;
    }
}
