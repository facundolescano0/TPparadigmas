package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.controller.*;
import org.example.funciones.FuncionesExtras;
import org.example.funciones.FuncionColorPrints;
import org.example.model.tipoCasilleros.Casillero;
import org.example.model.tipoCasilleros.CasilleroEjecutable;
import org.example.model.tipoCasilleros.TipoCasillero;
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

    //devuelve la casilla actual del jugador
    public int moverJugador(Jugador jugador, int dados){
        //vistaJuego.mostrarTurnoLibre(jugador.getNombre(), dados, colorANSI, resetColor);
        int casillaAnterior = jugador.getUbicacion();
        int casillaActual = administradorDeMovimientos.avanzarJugador(jugador, dados);
        //vistaJuego.mostrarUbicacion(casillaActual, resetColor);
        pagarBono(jugador, dados, casillaAnterior);
        return casillaActual;
    }
    public void ejecutarCasillero(Jugador jugador,int casillaActual){
        Casillero casillero = tablero.getCasillero(casillaActual);
        if (casillero.getEsEjecutable()) {
            CasilleroEjecutable casillero = tablero.getCasilleroEjecutable(casillaActual);
            //vistaJuego.mostrarMensaje(casillero.ejecutarCasillero(jugador));
        }
    }

//    private void ejecutar(Jugador jugador, int ubicacionJugador){
//        CasilleroEjecutable casillero = tablero.getCasilleroEjecutable(ubicacionJugador);
//        //vistaJuego.mostrarMensaje(casillero.ejecutarCasillero(jugador));
//    }

    public String evaluarAcciones(Jugador jugador){
        Scanner scanner = new Scanner(System.in);
        int numeroElecto = 1;
        while (numeroElecto != 0) {
            //vistaJuego.mostrarMensaje(colorANSI + "Seleccione la accion que quiere realizar indicando su numero (NUMERO):\n" + resetColor);
            String accion = scanner.nextLine();
            numeroElecto = corroboroAccion(accion);
            if (numeroElecto != Constantes.NEGATIVO) {
                Accion accionElecta = acciones.getAccion(numeroElecto);
                if(accionElecta == null || accionElecta == Accion.PAGAR_FIANZA || accionElecta == Accion.TIRAR_DADOS) {
                    //vistaJuego.mostrarAccionInexistente();
                } else{
                    ejecutarAccion(accionElecta, jugador);
                }
            }
        }if (jugador.estaEnDeuda()) {
            int ubicacion = jugador.getUbicacion();
            if(tablero.getCasillero(ubicacion).getTipo() == TipoCasillero.MULTA) {
                checkDeudaMulta(jugador);
            }else {
                Propiedad propiedad = tablero.getPropiedad(ubicacion).getPropiedad();
                checkDeudaComprable(jugador, propiedad);
            }
        }if (jugador.estaEnQuiebra()){
            vistaJuego.mostrarMensaje(String.format("%s perdio!\n", jugador.getNombre()));
            controlTablero.eliminarPropiedadesDelJugadorEnQuiebra(jugador);
            juego.eliminarJugador(jugador);
            juego.terminado();
        }
        if (checkGanarJugador.ComprobarGanarJugador(jugador)) {
            vistaJuego.mostrarMensaje("\t\t¡¡FELICITACIONES!!\nEl jugador "+ jugador.getNombre() + "ha completado todo un barrio con hoteles. Por eso, HA GANADO");
            this.terminado();
        }
    }

    public String realizarJuego(Jugador jugador){
        if (jugador.getEstado().equals(Estado.Preso)){
            return jugador.obtenerAccionesDisponibles() + juegoDePreso(jugador);
        }
        else{
            return jugador.obtenerAccionesDisponibles() + juegoDeLibre(jugador);
        }
    }

    public String juegoDeLibre(Jugador jugador){
        int dados = tirarDados();
        int casillaActual = moverJugador(jugador,dados);
        ejecutarCasillero(jugador,casillaActual);
        //String accionesDisponibles = empezarTurno(jugador);
        //Acciones acciones = new Acciones(); // ENLAZAR CON ACCIONES DISPONIBLES
        //acciones.accionesDisponibles(colorANSI, resetColor,jugador.getEstadoAcciones
        return ("Seleccione la accion que quiere realizar indicando su numero (NUMERO):\n") + evaluarAcciones(jugador);
    }

    private String juegoDePreso(Jugador jugador){
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
            return ejecutarAccion(accionElecta, jugador);
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


    private String ejecutarAccion(Accion accionElecta, Jugador jugador) {
        if (accionElecta == Accion.COMPRAR){
            return (fachada.comprar(jugador,0,controllConstrucciones));
        }else if (accionElecta != Accion.TERMINAR_TURNO && accionElecta != Accion.PAGAR_FIANZA){
            CheckStrToInt checkStrToInt = new CheckStrToInt();
            Scanner scanner = new Scanner(System.in);
            vistaJuego.mostrarMensaje("Seleccione el casillero en que se encuentra la propiedad (NUMERO):");
            String casillero = scanner.nextLine();
            int numero = (checkStrToInt.checkStringToInt(casillero));
            switch (accionElecta) {
                case CONSTRUIR -> vistaJuego.mostrarMensaje(fachada.construir(jugador, numero, controllConstrucciones));
                case VENDER -> vistaJuego.mostrarMensaje(fachada.vender(jugador, numero, controllConstrucciones));
                case HIPOTECAR -> vistaJuego.mostrarMensaje(fachada.hipotecar(jugador, numero, controllConstrucciones));
                case DESHIPOTECAR -> vistaJuego.mostrarMensaje(fachada.deshipotecar(jugador, numero, controllConstrucciones));
                case CONSULTAR_PRECIO_CASA -> vistaJuego.mostrarMensaje(fachada.consultar_precio_casa(jugador, numero, controllConstrucciones));
            }
        }
        else if (accionElecta == Accion.PAGAR_FIANZA){
            return(fachada.pagar_fianza(jugador, tablero.getCarcel()));
        }
        return null;
    }
}
