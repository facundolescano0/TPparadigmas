package org.example.controller;

import org.example.model.Jugador;

public interface EjecutarAccion {
    String ejecutar(Jugador jugador, int propiedad,ConstruccionController controller);
}
