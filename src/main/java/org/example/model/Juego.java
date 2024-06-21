package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.controller.*;
import org.example.funciones.FuncionesExtras;
import org.example.funciones.FuncionColorPrints;
import org.fusesource.jansi.Ansi;

public class Juego {
    private ArrayList<Jugador> jugadores;
    private Tablero tablero;
    private AdministradorTurnos administradorDeTurnos;
    private AdministradorDeMovimientos administradorDeMovimientos;
    private Configuracion configuracion;
    private Banco banco;
    private FachadaAcciones fachada;
    private FuncionesExtras funcionesExtras;


    public Juego(List<String> configuraciones){
        Configuracion configuracion = new Configuracion(configuraciones);
        this.configuracion = configuracion;
        jugadores = configuracion.getJugadores();
        this.administradorDeTurnos = new AdministradorTurnos(jugadores);
        this.tablero = new Tablero(configuracion);
        this.banco = new Banco(configuracion.getMontoVuelta());
        this.administradorDeMovimientos = new AdministradorDeMovimientos(tablero);
        this.funcionesExtras = new FuncionesExtras(tablero);
        this.fachada = new FachadaAcciones(new Hipotecar(funcionesExtras),new Comprar(funcionesExtras),new Vender(funcionesExtras),new ConsultarPrecios(funcionesExtras),new Construir(funcionesExtras),new Deshipotecar(funcionesExtras),new PagarFianza());

    }

    public Jugador getJugadorActual() {
       return administradorDeTurnos.getTurnoActual();
    }

    public Boolean terminado() {
        return checkEstadoJugadores(jugadores);
    }

    public void eliminarJugador(Jugador jugador){
        jugadores.remove(jugador);
    }

    public void cambiarTurno() {
        administradorDeTurnos.avanzarTurno();
    }

    public int tirarDados(){
        return administradorDeTurnos.tirarDados(configuracion.getCantidadDeLadosEnDado());
    }

    public Tablero getTablero() {
        return this.tablero;
    }

    public ArrayList<Jugador> getJugadores() {
        return this.jugadores;
    }

    public void pagarBono(Jugador jugador){
        banco.pagarBono(jugador);
    }

    public boolean checkEstadoJugadores(List<Jugador> jugadores){
        for(Jugador jugador: jugadores){
            if (jugador.getEstado().equals(Estado.Gano)){
                return true;
            }
        }
        return jugadores.size() == 1;
    }
    public String empezarTurno(Jugador jugador){
        Ansi colorANSI = null;
        Ansi resetColor = Ansi.ansi().reset();
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        colorANSI = funcionColorPrints.obtenerColorANSI(jugador.getColor());
        String acciones = jugador.obtenerAccionesDisponibles();
        return acciones;
    }

    public void realizarJuego(Jugador jugador){
        if (jugador.getEstado().equals(Estado.Preso)){
            juegoDePreso(jugador);
        }else{
            int dados = tirarDados();
            jugador.mover(dados);
            empezarTurno(jugador);
        }
    }

    private void juegoDePreso(Jugador jugador){
        Scanner scanner = new Scanner(System.in);
        int numeroElecto = -1;
        while ((numeroElecto != 0) && (numeroElecto != 1) && (jugador.getCondena() != 0)) {
            String accion = scanner.nextLine();
            numeroElecto = corroboroAccion(accion);
        }
        if (numeroElecto == -1) {
            FuncionesExtras.delay(1000);
    //        vistaJuego.mostrarMensaje("El jugador " + jugador.getNombre() + " complió su condena!");
            FuncionesExtras.delay(1000);
            jugador.setEstado(Estado.EnJuego);
            jugador.actualizarEstadoAcciones();

        }else if(numeroElecto == 1) {
            Accion accionElecta = new Acciones().getAccionPreso(numeroElecto);
            ejecutarAccion(accionElecta, jugador);
        }else{
            int dados = tirarDados();
            FuncionesExtras.delay(1000);
    //        vistaJuego.mostrarMensaje("Saco: " + dados);
            FuncionesExtras.delay(1500);
            if (dados > jugador.getCondena()) {
                jugador.quedaLibre();
    //            vistaJuego.mostrarMensaje(jugador.getNombre() + " queda libre por sacar " + dados + " (dados) mayor que el numero de condena (" + jugador.getCondena() + ")");
            }else {
                jugador.restarCondena();
    //            vistaJuego.mostrarMensaje(jugador.getNombre() + " sigue preso. Ahora su condena es de (" + jugador.getCondena() + ")");
            }
            FuncionesExtras.delay(1000);
        }

    }


    private int corroboroAccion(String accion) {
        CheckStrToInt checkStrToInt = new CheckStrToInt();
        return checkStrToInt.checkStringToInt(accion);
    }

}
