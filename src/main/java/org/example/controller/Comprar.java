package org.example.controller;

import org.example.funciones.FuncionesExtras;
import org.example.model.Comprable;
import org.example.model.Jugador;

public class Comprar implements EjecutarAccion{
    private FuncionesExtras funcionesExtras;

    public Comprar(FuncionesExtras func){
        this.funcionesExtras = func;
    }

    public String ejecutar(Jugador jugador, int propiedad, ConstruccionController controller) {
        int ubicacionJugador = jugador.getUbicacion();
        if (funcionesExtras.esComprable(ubicacionJugador)) {
            Comprable comprable = funcionesExtras.obtenerComprable(ubicacionJugador);
            Jugador propietario = comprable.getPropietario();
            if (propietario == null) {
                return jugador.comprarComprable(comprable);
            }else{
                return ("No se puede comprar. Esta propiedad ya pertenece a " + propietario.getNombre());
            }
        }else{
            return ("No se puede comprar. Esta casilla no es comprable");
        }
        return null;
    }
}
