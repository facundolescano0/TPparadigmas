package org.example.controller;

import org.example.model.Colores;
import org.example.model.Jugador;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;


public class Configuracion {
    private int montoVuelta;
    private int montoInicial;
    private int cantidadDeDados;
    private int condenaCarcel;
    private final int montoDeMulta;
    private int cantidadCasilleros;
    private int montoFianza;
    private ArrayList<Jugador> jugadores;


    public Configuracion(List<String> configuraciones) {
        cantidadCasilleros = Integer.parseInt(configuraciones.get(CheckArgumentos
                .ConfiguracionCheckArgumentos.CASILLEROS.ordinal()));
        montoInicial = Integer.parseInt(configuraciones.get(CheckArgumentos
                .ConfiguracionCheckArgumentos.DINERO_INICIAL.ordinal()));
        System.out.println(montoInicial);
        cantidadDeDados= Integer.parseInt(configuraciones.get(CheckArgumentos
                .ConfiguracionCheckArgumentos.CANTIDAD_DE_DADOS.ordinal()));
        montoVuelta = Integer.parseInt(configuraciones.get(CheckArgumentos
                .ConfiguracionCheckArgumentos.DINERO_VUELTA.ordinal()));
        condenaCarcel = Integer.parseInt(configuraciones.get(CheckArgumentos
                .ConfiguracionCheckArgumentos.TURNOS_PRESO.ordinal()));
        montoDeMulta = Integer.parseInt(configuraciones.get(CheckArgumentos
                .ConfiguracionCheckArgumentos.MULTA.ordinal()));
        montoFianza = Integer.parseInt(configuraciones.get(CheckArgumentos
                .ConfiguracionCheckArgumentos.FIANZA.ordinal()));

        ArrayList<Jugador> jugadores = this.crearJugadores(configuraciones);
        this.jugadores = jugadores;
        asignacionColores(jugadores);
    }

    private ArrayList<Jugador> crearJugadores(List<String> configuraciones){
        List<String> nombres = List.of(configuraciones.get(0).split(" "));
        int plataInicial = Integer.parseInt(configuraciones.get(CheckArgumentos.ConfiguracionCheckArgumentos.DINERO_INICIAL.ordinal()));
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        for (String s : nombres) {
            Jugador jugador = new Jugador(s);
            jugador.setPlata(plataInicial);
            jugadores.add(jugador);
        }

        return jugadores;
    }

    private void asignacionColores(List<Jugador> jugadores){
        Colores.Color[] colores = Colores.Color.values();
        for(int i = 0; i < jugadores.size(); i++){
            jugadores.get(i).setColor(colores[i]);
        }
    }
    public int getMontoVuelta() {
        return montoVuelta;
    }

    public double getMontoInicial(){
        return (double) montoInicial;
    }

    public ArrayList<Jugador> getJugadores() { return jugadores;}

    public int getMontoFianza() { return montoFianza; }

    public int getCantidadCasilleros() { return cantidadCasilleros; }

    public int getMontoPorPasarSalida(){return montoVuelta;}

    public int getCondenaCarcel(){return condenaCarcel;}

    public int getMontoMulta(){return montoDeMulta;}

    public int getCantidadDeDados(){ return cantidadDeDados;}
}
