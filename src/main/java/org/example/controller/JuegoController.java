package org.example.controller;

import org.example.funciones.FuncionColorPrints;
import org.example.funciones.FuncionesExtras;
import org.example.model.*;
import org.example.view.JugadorView;
import org.example.view.TableroView;
import org.fusesource.jansi.Ansi;
import org.example.view.JuegoView;
import java.io.IOException;


public class JuegoController implements Suscriptor {
    private final Juego juego;
    private JuegoView vistaJuego;
    private AdministradorDeMovimientos administradorDeMovimientos;
    private Tablero tablero;
    private ConstruccionController controllConstrucciones;
    private FachadaAcciones fachada;
    private TableroController controlTablero;
    private FuncionesExtras funcionesExtras;
    private TableroView tableroView;
    private JugadorView jugadorView;


    public JuegoController(Juego juego) throws IOException {
        this.juego = juego;
        this.tablero = juego.getTablero();
        this.vistaJuego = new JuegoView(juego);
        this.tableroView = new TableroView(tablero);

        this.controllConstrucciones = new ConstruccionController(tablero.getBarrios());
        this.funcionesExtras = new FuncionesExtras(tablero);
        this.jugadorView = new JugadorView(juego.getJugadores());
    }

    public void jugarTurno() throws IOException {
        juego.cambiarTurno();
        Jugador jugador = juego.getJugadorActual();
        Ansi colorANSI = null;
        Ansi resetColor = Ansi.ansi().reset();
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        colorANSI = funcionColorPrints.obtenerColorANSI(jugador.getColor());

        juego.realizarJuego(jugador,colorANSI);
    }

    @Override
    public void recibirNoticia(String mensaje) {
        vistaJuego.imprimirMensajes(mensaje);
    }

    @Override
    public void recibirEstadoJugadores() {
        vistaJuego.mostrar();
    }

}