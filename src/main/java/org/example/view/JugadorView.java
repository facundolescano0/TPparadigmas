package org.example.view;



import org.example.funciones.FuncionColorPrints;

import java.util.ArrayList;
import java.util.List;

import org.example.model.Jugador;
import org.fusesource.jansi.Ansi;

public class JugadorView {
    private final List<Jugador> jugadores;

    public JugadorView(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public void mostrarJugadores() {
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        for (Jugador jugador : jugadores) {
        Ansi colorANSI = funcionColorPrints.obtenerColorANSI(jugador.getColor());
        Ansi resetColor = Ansi.ansi().reset();
        System.out.println("=======================================");
        System.out.println("Nombre: " + colorANSI + jugador.getNombre() + resetColor);
        System.out.println("Color: " + colorANSI + jugador.getColor().toString() + resetColor);
        System.out.println("Plata: " + colorANSI + jugador.getPlata() + resetColor);
        System.out.println("Ubicación: " + colorANSI + jugador.getUbicacion() + resetColor);
        System.out.println("Estado: " + colorANSI + jugador.getEstado().toString() + resetColor);
        System.out.println("Condena: " + colorANSI + jugador.getCondena() + resetColor);
        System.out.println("=======================================");
        }
    }

}
