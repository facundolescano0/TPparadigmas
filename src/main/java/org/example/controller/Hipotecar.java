package org.example.controller;

import org.example.model.Propiedad;
import org.example.model.Jugador;
import org.example.model.Tablero;
import org.example.funciones.FuncionesExtras;

public class Hipotecar implements  EjecutarAccion{
    private FuncionesExtras funcionesExtras;

    public Hipotecar(FuncionesExtras func){
        this.funcionesExtras = func;
    }
    public String ejecutar(Jugador jugador, int propiedad, ConstruccionController controller) {
        Propiedad prop = funcionesExtras.obtenerPropiedadJugador(propiedad,jugador);
        if (prop != null) {
            Tablero tablero = funcionesExtras.getTablero();
            return jugador.hipotecarPropiedad(tablero.getBarrio(prop),prop);

        }else {
            return ("No se puede hipotecar Esta propiedad no pertenece a " + jugador.getNombre());
        }
    }
}