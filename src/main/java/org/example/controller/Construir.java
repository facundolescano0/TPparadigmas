package org.example.controller;

import org.example.funciones.FuncionesExtras;
import org.example.model.Jugador;
import org.example.model.Propiedad;

public class Construir implements EjecutarAccion{
    private FuncionesExtras funciones;

    public Construir(FuncionesExtras func){
        this.funciones = func;
    }

    public String ejecutar(Jugador jugador, int propiedad, ConstruccionController controller) {
        Propiedad prop = funciones.obtenerPropiedadJugador(propiedad, jugador);
        if (prop != null) {
            return controller.construirEnPropiedad(jugador, prop);
        }else{
            return "Accion imposible de realizar";
        }
    }
}