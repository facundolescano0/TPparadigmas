package org.example;


import org.example.controller.JuegoController;
import org.example.model.*;
import org.example.controller.CheckArgumentos;
import org.example.model.tipoCasilleros.Casillero;
import org.example.view.JugadorView;
import org.example.view.TableroView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        CheckArgumentos check = new CheckArgumentos();
        check.CheckArgumentos();
        List<String> argumentos = check.getConfiguraciones();
        Juego juego = new Juego(argumentos);
        Tablero tablero = juego.getTablero();
        TableroView tableroView = new TableroView(tablero);
        List<Jugador> listaJugadores = juego.getJugadores();
        JuegoController juegoController = new JuegoController(juego, tableroView);
        JugadorView jugadorView = new JugadorView(listaJugadores.get(0));
        JugadorView jugadorView2 = new JugadorView(listaJugadores.get(1));
        JugadorView jugadorView3 = new JugadorView(listaJugadores.get(2));
        JugadorView jugadorView4 = new JugadorView(listaJugadores.get(3));
        jugadorView.mostrarJugador();
        jugadorView2.mostrarJugador();
        jugadorView3.mostrarJugador();
        jugadorView4.mostrarJugador();

        while (!juego.terminado()){
            juegoController.jugarTurno();
        }
        System.out.println("El juego terminado");
    }
    

}

/*
QUE TENEMOS:
    CREAMOS EL JUEGO
    CREAMOS LAS CONFIGURACIONES
    CREAMOS LOS JUGADORES
    GENERAMOS UN ORDEN DE JUEGO

COSAS PENDIENTES:
*       TIRAR DADO
*       CREAR TABLERO
*       MOSTRAR TABLERO
*
*       OPCIONES DEL USUARIO
        IMPLEMENTAR BANCO
        IMPLEMENTAR BANCARROTA
        IMPLEMENTAR FIN DE PARTIDA
*
* */


