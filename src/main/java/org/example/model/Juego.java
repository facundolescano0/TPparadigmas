package org.example.model;

import java.text.CompactNumberFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import org.example.controller.CheckArgumentos;
import org.example.controller.CheckArgumentos.ConfiguracionCheckArgumentos;

import org.example.model.Colores;
import java.util.concurrent.ScheduledExecutorService;

public class Juego {
    private List<Jugador> jugadores;
    private Tablero tablero;
    private AdministradorTurnos administradorDeTurno;
    private AdministradorDeMovimientos administradorDeMovimientos;

    public Juego(List<String> configuraciones){
        List<Jugador> jugadores = this.crearJugadores(configuraciones);
        this.jugadores = jugadores;
        administradorDeTurno = new AdministradorTurnos(jugadores);
        administradorDeTurno.generarOrdenDePaso();
        this.tablero = new Tablero(Integer.parseInt(configuraciones.get(ConfiguracionCheckArgumentos.CASILLEROS.ordinal())));
        this.administradorDeMovimientos = new AdministradorDeMovimientos(tablero);
        this.administradorDeTurno = new AdministradorTurnos(jugadores);
        this.asignacionColores(jugadores);
    }


    private void asignacionColores(List<Jugador> jugadores){
        Colores.Color[] colores = Colores.Color.values();
        for(int i = 0; i < jugadores.size(); i++){
            jugadores.get(i).setColor(colores[i]);
        }

    }
    public ArrayDeque<Jugador> creacionColaTurnos(){
        ArrayDeque<Jugador> colaTurnos = new ArrayDeque<>();
        for (Jugador jugadore : jugadores) {
            colaTurnos.addLast(jugadore);
        }
        return colaTurnos;
    }

//    public void setJugador(Jugador jugador, int casilla){
//        tablero.avanzarJugador(casilla);
//    }
//    public Jugador getJugadorActual() {
//        return administradorTurnos.obtenerJugador();
//    }

    public Boolean terminado() {
        return checkEstadoJugadores(jugadores);
    }


    public Tablero getTablero() {
        return this.tablero;
    }

    public List<Jugador> getJugadores() {
        return this.jugadores;
    }

    public boolean checkEstadoJugadores(List<Jugador> jugadores){
        int jugadoresFuera = 0;
        for(Jugador jugador: jugadores){
            if (jugador.estaEnQuiebra()){
                jugadoresFuera++;
            }
        }
        return jugadoresFuera == jugadores.size()-1;
    }

    private List<Jugador> crearJugadores(List<String> configuraciones){
        List<String> nombres = List.of(configuraciones.get(0).split(" "));
        int plataInicial = Integer.parseInt(configuraciones.get(2));
        List<Jugador> jugadores = new ArrayList<Jugador>();
        for (String s : nombres) {
            Jugador jugador = new Jugador(s);
            jugador.setPlata(plataInicial);
            jugadores.add(jugador);
        }

        return jugadores;
    }
    public void cambiarTurno(){ administradorDeTurno.avanzarTurno(); }
}
