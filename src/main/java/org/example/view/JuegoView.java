package org.example.view;

import org.example.funciones.FuncionesExtras;
import java.util.ArrayList;
import org.example.controller.*;
import org.example.model.Acciones;
import org.example.model.Juego;
import org.example.model.Jugador;
import org.fusesource.jansi.Ansi;


public class JuegoView {
    private ArrayList<Jugador> jugadores;
    private TableroView tableroView;

    public JuegoView(Juego juego){
        this.jugadores = juego.getJugadores();
        this.tableroView = new TableroView(juego.getTablero());
    }

    public void mostrar() {
        FuncionesExtras.delay(0000);
        tableroView.mostrar(jugadores);
        FuncionesExtras.delay(0000);
        JugadorView jugadorView = new JugadorView(jugadores);
        jugadorView.mostrarJugadores();
        FuncionesExtras.delay(0000);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarAccionesPreso(String nombreJugador, Ansi colorANSI, Ansi resetColor) {
        System.out.println(colorANSI + "Es el turno de " + nombreJugador + "\n");
        Acciones acciones = new Acciones();
        acciones.accionesJugadorPreso(colorANSI, resetColor);
    }

    public void mostrarFinTurno(Jugador jugador){
        System.out.println("Finalizó el turno de " + jugador.getNombre());
    }

    public void mostrarTurnoLibre(String nombreJugador, int dados, Ansi colorANSI, Ansi resetColor) {
        System.out.println(colorANSI + "Es el turno de " + nombreJugador + "\n" + "Tus dados son: " + dados + "\n");
    }

    public void mostrarUbicacion(int casillaActual, Ansi resetColor) {
        System.out.println("Usted esta en el casillero: " + casillaActual + "\n" + resetColor);
    }

    public void mostrarAccionInexistente() {
        System.out.println("Accion inexistente");
    }

    public void terminarJuego(){
        System.out.println("Juego terminado");
    }
}