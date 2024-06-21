package org.example.controller;

import org.example.funciones.FuncionesExtras;
import org.example.model.EstacionTransporte;
import org.example.model.Jugador;
import org.example.model.Propiedad;
import org.example.model.Comprable;

public class Vender implements EjecutarAccion {
    private FuncionesExtras funciones;

    public Vender(FuncionesExtras func) {
        this.funciones = func;
    }

    @Override
    public String ejecutar(Jugador jugador, int casilleroComprable, ConstruccionController controller) {
        Comprable comprable = funciones.ChequearComprableYPropietarioJugador(casilleroComprable, jugador);
        if (comprable != null) {
            if (comprable.getEsPropiedad()) {
                return controller.vender(jugador, (Propiedad) comprable);
            } else {
                return jugador.venderEstacion(comprable);
            }
        }
        return null;
    }
}
