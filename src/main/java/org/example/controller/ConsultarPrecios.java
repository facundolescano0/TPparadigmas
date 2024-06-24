package org.example.controller;

import org.example.funciones.FuncionesExtras;
import org.example.model.Jugador;
import org.example.model.Propiedad;

public class ConsultarPrecios implements EjecutarAccion{
    private FuncionesExtras funcionesExtras;

    public ConsultarPrecios(FuncionesExtras func){
        this.funcionesExtras = func;
    }

    public String ejecutar(Jugador jugador, int casilleroPropiedad, ConstruccionController controller){
        Propiedad propiedad = funcionesExtras.obtenerPropiedad(casilleroPropiedad);
        if (propiedad != null) {
            System.out.println("El precio de una casa en esa propiedad es " + propiedad.getPrecioCasa());
        }
        return null;
    }
}
