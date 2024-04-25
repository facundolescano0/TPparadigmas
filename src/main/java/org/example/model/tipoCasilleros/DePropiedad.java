package org.example.model.tipoCasilleros;

import org.example.model.*;
import org.example.model.EstadoPropiedades;

import java.io.Serializable;

public class DePropiedad extends Casillero implements CasilleroEjecutable {
    private final Propiedad propiedad;

    public DePropiedad(int casillero, int precio, int numeroDeBarrio) {
        super("Propiedad", TipoCasillero.PROPIEDAD, casillero);
        this.propiedad = new Propiedad(precio, numeroDeBarrio, casillero);
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public double getPrecio() {
        return propiedad.getPrecio();
    }
    @Override
    public void ejecutarCasillero(Jugador jugador) {
        if (propiedad.getEstado() == EstadoPropiedades.COMPRADO && propiedad.getPropietario() != jugador) {
            if(jugador.getPatrimonioTotal() < propiedad.getAlquiler()) {
                System.out.println();
                System.out.println("EL JUGADOR " + jugador.getNombre() + "ENTRÓ EN BANCARROTA. SIN DINERO SUFICIENTE.");
                jugador.setQuiebra();
            }else{
                double alquiler = propiedad.getAlquiler();

                if (jugador.restarPlata(alquiler)){
                    System.out.printf("%s pagaste %f de alquiler por estar en la propiedad de %s\n",jugador.getNombre(),alquiler,propiedad.getNombrePropietario());
                }else{
                    System.out.println(jugador.getNombre() +"¡no tienes dinero suficiente para pagar el alquiler de esta propiedad!\n\tDEBES HIPOTECAR SI O SI ANTES DE AVANZAR, SINO VA A PERDER");
                    System.out.println("Antes de avanzar el turno debes tener $" + propiedad.getPrecio() + " sino perderás automaticamente. La deuda se paga al final de su turno.");
                    jugador.setDeuda();
                }
            }
        }

    }
}


