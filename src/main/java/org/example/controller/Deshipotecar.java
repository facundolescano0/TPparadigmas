package org.example.controller;

import org.example.funciones.FuncionesExtras;
import org.example.model.Propiedad;
import org.example.model.Jugador;

public class Deshipotecar implements EjecutarAccion{
    private FuncionesExtras funciones;

    public Deshipotecar(FuncionesExtras func){
        this.funciones = func;
    }

    public String ejecutar(Jugador jugador, int propiedad, ConstruccionController controller) {
        Propiedad prop = funciones.obtenerPropiedadJugador(propiedad,jugador);
        if (prop != null) {
            return jugador.deshipotecarPropiedad(prop);
        }
        return null;
    }
}
