package org.example.funciones;

import org.example.model.*;
import org.example.model.tipoCasilleros.DePropiedad;
import org.example.model.tipoCasilleros.Estacion;
import org.example.model.tipoCasilleros.TipoCasillero;
import java.util.List;


public class FuncionesExtras {
    private Tablero tablero;

    public FuncionesExtras(Tablero tablero) {
        this.tablero = tablero;
    }

    public static void delay(int tiempo) {
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void delayPrint(String text, int delayMillis) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("");
    }

    public Propiedad obtenerPropiedad(int casillero) {
        if (esPropiedad(casillero)) {
            DePropiedad casilleroPropiedad = tablero.getPropiedad(casillero);
            return casilleroPropiedad.getPropiedad();
        }
        System.out.println("Accion imposible de realizar");
        return null;
    }

    public Propiedad obtenerPropiedadJugador(int casillero, Jugador jugador) {
        if (esPropiedad(casillero)) {
            List<Propiedad> propiedadList = jugador.getPropiedades();
            for (Propiedad propiedad : propiedadList) {
                if (propiedad.getUbicacion() == casillero) {
                    return propiedad;
                }
            }
            return null;
        }
        return null;
    }

    public Comprable obtenerComprable(int casillero) {
        if(!esComprable(casillero)){
            System.out.println("Accion imposible de realizar");
            return null;
        }
        Comprable comprable = null;
        if(esPropiedad(casillero)){
            DePropiedad casilleroPropiedad = tablero.getPropiedad(casillero);
            comprable = casilleroPropiedad.getPropiedad();
            return comprable;
        }
        Estacion casilleroEstacion = tablero.getEstacion(casillero);
        comprable = casilleroEstacion.getEstacion();
        return comprable;
    }

    public Comprable ChequearComprableYPropietarioJugador(int casillero, Jugador jugador){
        Comprable comprable = obtenerComprable(casillero);
        if(comprable != null && comprable.getPropietario()==jugador){
            return comprable;
        }
        if (comprable == null){
            System.out.println("El casillero no es un objeto comprable.");
        }else{
            System.out.println("El jugador " + jugador.getNombre() + " no es dueño de esta ubicación");
        }
        return null;
    }

    public boolean esPropiedad(int casillero) {
        if (casillero < tablero.getCantidadCasilleros()) {
            TipoCasillero tipoCasillero = tablero.getTipoCasillero(casillero);
            return tipoCasillero == TipoCasillero.PROPIEDAD;
        }
        return false;
    }

    public boolean esComprable(int casillero) {
        TipoCasillero tipoCasillero = tablero.getTipoCasillero(casillero);
        return tipoCasillero == TipoCasillero.ESTACION ||
                tipoCasillero == TipoCasillero.PROPIEDAD;
    }

    public Tablero getTablero(){
        return tablero;
    }

}