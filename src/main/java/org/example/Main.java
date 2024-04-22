package org.example;


import org.example.controller.Configuracion;
import org.example.controller.JuegoController;
import org.example.model.*;
import org.example.controller.CheckArgumentos;
import org.example.model.tipoCasilleros.Casillero;
import org.example.view.JugadorView;

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
        JuegoController juegoController = new JuegoController(juego);
        while (!juego.terminado()){
            juegoController.jugarTurno();
        }
        check.CerrarScanner();
        System.out.println("El juego terminado");
    }
}
/*
COSAS PARA HACER
ver si no conviene que el usuario eliga la cant de dada

-carcel: si cae en IR a la carcel, modificar ubicacion de jugador para que vaya la a la carcel
y limitar las acciones(habria que hacer casi lo mismo que cuando se juega un turno sin
 que el jugador este preso. y que tenga solo las opciones terminar turno y pagar fianza)

*/


