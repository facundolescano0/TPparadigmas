package org.example.controller;

import org.example.model.Acciones;
import org.example.model.Juego;
import org.example.model.Jugador;
import org.example.view.TableroView;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal;

import java.io.IOException;

public class JuegoController {
    private final Juego juego;
    private final TableroView tableroView;
    private final LineReader reader;

    public JuegoController(Juego juego, TableroView tableroView) throws IOException {
        this.juego = juego;
        this.tableroView = tableroView;

        Terminal terminal = TerminalBuilder.terminal();
        reader = LineReaderBuilder.builder().terminal(terminal).build();
    }

    public void jugarTurno() {
        tableroView.mostrar();
        int numeroElecto = 1;
        while(numeroElecto != 0){
            Acciones.mostrarAcciones();
            String accion = reader.readLine("Seleccione la accion que quiere realizar indicando su numero (NUMERO):");
            numeroElecto = Integer.parseInt(accion);
            Acciones.Accion accionElecta = Acciones.Accion.getAccion(numeroElecto);
            if(accionElecta == null) System.out.println("Accion inexistente");
            //realizar accion
        }

        juego.cambiarTurno();

    }
}
//Nico: no va aca sino en el del view, ver tatedrez
//    public void setJuego(Juego juego){
//        this.juego = juego;
//        List<Jugador> jugadores = juego.getJugadores();
//        Jugador jugador1 = jugadores.get(0);
//        Jugador jugador2 = jugadores.get(1);
//        this.jugador1.setText(jugadores.get(0).getNombre());
//        this.jugador2.setText(jugadores.get(1).getNombre());
//        this.jugador1_color.setFill(colores.get(jugador1.getColor()));
//        this.jugador2_color.setFill(colores.get(jugador2.getColor()));
//        this.jugadorActual.setText("Jugador actual: " + juego.getJugadorActual().getNombre());
//        tableroController.setJuego(juego);
//    }

