package org.example.view;

import org.example.funciones.FuncionesExtras;
import java.util.ArrayList;
import org.example.controller.*;
import org.example.model.Juego;
import org.example.model.Jugador;

public class JuegoView {
    private ArrayList<Jugador> jugadores;
    private TableroView tableroView;

    public JuegoView( Juego juegoController){
        this.jugadores = juegoController.getJugadores();
        this.tableroView = new TableroView(juegoController.getTablero());

    }

    public void mostrar() {
        FuncionesExtras.delay(0000);
        tableroView.mostrar(jugadores);
        FuncionesExtras.delay(0000);
        JugadorView jugadorView = new JugadorView(jugadores);
        jugadorView.mostrarJugadores();
        FuncionesExtras.delay(0000);
        System.out.println();

    }
    public void terminarJuego(){
        System.out.println("Juego terminado");
    }
}
