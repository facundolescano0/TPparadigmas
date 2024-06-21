package org.example.controller;

import org.example.funciones.FuncionColorPrints;
import org.example.funciones.FuncionesExtras;
import org.example.model.*;
import org.example.model.tipoCasilleros.*;
import org.fusesource.jansi.Ansi;
import org.example.view.JuegoView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.example.model.Accion;

public class JuegoController {
    private final Juego juego;
    private JuegoView vistaJuego;

    private Tablero tablero;
    private ConstruccionController controllConstrucciones;
    private FachadaAcciones fachada;
    private TableroController controlTablero;
    private FuncionesExtras funcionesExtras;
    private CheckGanarJugador checkGanarJugador;

    public JuegoController(Juego juego) throws IOException {
        this.juego = juego;
        this.tablero = juego.getTablero();
        this.vistaJuego = new JuegoView(juego);
        this.controlTablero = new TableroController(tablero);

        this.controllConstrucciones = new ConstruccionController(tablero.getBarrios());
        this.funcionesExtras = new FuncionesExtras(tablero);
        this.checkGanarJugador= new CheckGanarJugador(tablero);
        this.fachada = new FachadaAcciones(new Hipotecar(funcionesExtras),new Comprar(funcionesExtras),new Vender(funcionesExtras),new ConsultarPrecios(funcionesExtras),new Construir(funcionesExtras),new Deshipotecar(funcionesExtras),new PagarFianza());
    }

    public void jugarTurno() throws IOException {
        juego.cambiarTurno();
        Jugador jugador = juego.getJugadorActual();
        if(jugador.getEstado() == Estado.EnJuego){
            jugarTurnoLibre(jugador);
        } else if(jugador.getEstado() == Estado.Preso){
            jugarTurnoPreso(jugador);
        }else{
            vistaJuego.mostrarMensaje("El jugador " + jugador.getNombre() + " ha perdido!");
            juego.eliminarJugador(jugador);
            juego.cambiarTurno();
        }
    }

    private void jugarTurnoPreso(Jugador jugador){



        if (numeroElecto == -1) {
            FuncionesExtras.delay(1000);
            vistaJuego.mostrarMensaje("El jugador " + jugador.getNombre() + " complió su condena!");
            FuncionesExtras.delay(1000);
            jugador.setEstado(Estado.EnJuego);
        }else if(numeroElecto == 1) {
            Accion accionElecta = new Acciones().getAccionPreso(numeroElecto);
            ejecutarAccion(accionElecta, jugador);
        }else{
            int dados = juego.tirarDados();
            FuncionesExtras.delay(1000);
            vistaJuego.mostrarMensaje("Saco: " + dados);
            FuncionesExtras.delay(1500);
            if (dados > jugador.getCondena()) {
                jugador.quedaLibre();
                vistaJuego.mostrarMensaje(jugador.getNombre() + " queda libre por sacar " + dados + " (dados) mayor que el numero de condena (" + jugador.getCondena() + ")");
            }else {
                jugador.restarCondena();
                vistaJuego.mostrarMensaje(jugador.getNombre() + " sigue preso. Ahora su condena es de (" + jugador.getCondena() + ")");
            }
            FuncionesExtras.delay(1000);
        }
        if(jugador.getEstado() == Estado.EnJuego) {
            jugarTurnoLibre(jugador);
        }
    }


    private void jugarTurnoJugador(Jugador jugador){
        //----------------Variables para el color----------------------------
        Ansi colorANSI = null;
        Ansi resetColor = Ansi.ansi().reset();
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        colorANSI = funcionColorPrints.obtenerColorANSI(jugador.getColor());
        //-------------------------------------------------------------------

        String accionesDelJugador =  juego.empezarTurno(jugador);
        vistaJuego.mostrarMensaje(accionesDelJugador);
        vistaJuego.mostrarMensaje(colorANSI + "Seleccione la accion que quiere realizar indicando su numero (NUMERO):\n" + resetColor);
        //vistaJuego.mostrarUbicacion(casillaActual, resetColor);
        juego.realizarJuego(jugador);

    }

    private void jugarTurnoLibre(Jugador jugador) {
        int dados = juego.tirarDados();
        Ansi colorANSI = null;
        Ansi resetColor = Ansi.ansi().reset();
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        colorANSI = funcionColorPrints.obtenerColorANSI(jugador.getColor());

        vistaJuego.mostrarTurnoLibre(jugador.getNombre(), dados, colorANSI, resetColor);
        int casillaAnterior = jugador.getUbicacion();
        int casillaActual = administradorDeMovimientos.avanzarJugador(jugador, dados);
        vistaJuego.mostrarUbicacion(casillaActual, resetColor);
        pagarBono(jugador, dados, casillaAnterior);
        Casillero casillero = tablero.getCasillero(casillaActual);

        if (casillero.getEsEjecutable()) {
            ejecutar(jugador, casillaActual);
        }

        if (jugador.getEstado() == Estado.Preso) return;
        int numeroElecto = 1;
        vistaJuego.mostrar();
        Acciones acciones = new Acciones();
        acciones.acciones(colorANSI, resetColor);
        Scanner scanner = new Scanner(System.in);
        while (numeroElecto != 0) {
            vistaJuego.mostrarMensaje(colorANSI + "Seleccione la accion que quiere realizar indicando su numero (NUMERO):\n" + resetColor);
            String accion = scanner.nextLine();
            numeroElecto = corroboroAccion(accion);
            if (numeroElecto == Constantes.NEGATIVO) {
                vistaJuego.mostrarAccionInexistente();
            }else {
                Accion accionElecta = acciones.getAccion(numeroElecto);
                if(accionElecta == null || accionElecta == Accion.PAGAR_FIANZA || accionElecta == Accion.TIRAR_DADOS) {
                    vistaJuego.mostrarAccionInexistente();
                } else{
                    ejecutarAccion(accionElecta, jugador);
                }
            }
        }
        if (jugador.estaEnDeuda()) {
            int ubicacion = jugador.getUbicacion();
            if(tablero.getCasillero(ubicacion).getTipo() == TipoCasillero.MULTA) {
                checkDeudaMulta(jugador);
            }else {
                Propiedad propiedad = tablero.getPropiedad(ubicacion).getPropiedad();
                checkDeudaComprable(jugador, propiedad);
            }
        }
        if (jugador.estaEnQuiebra()){
            vistaJuego.mostrarMensaje(String.format("%s perdio!\n", jugador.getNombre()));
            controlTablero.eliminarPropiedadesDelJugadorEnQuiebra(jugador);
            juego.eliminarJugador(jugador);
            juego.terminado();
        }
        if (checkGanarJugador.ComprobarGanarJugador(jugador)) {
            vistaJuego.mostrarMensaje("\t\t¡¡FELICITACIONES!!\nEl jugador "+ jugador.getNombre() + "ha completado todo un barrio con hoteles. Por eso, HA GANADO");
            juego.terminado();
        }
    }

    private void ejecutarAccion(Accion accionElecta, Jugador jugador) {
            if (accionElecta == Accion.COMPRAR){
                vistaJuego.mostrarMensaje(fachada.comprar(jugador,0,controllConstrucciones));
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
            if (accionElecta == Accion.PAGAR_FIANZA){
                vistaJuego.mostrarMensaje(fachada.pagar_fianza(jugador, tablero.getCarcel()));
            }
    }

    private void pagarBono(Jugador jugador,int dados,int casillaAnterior){
            if((casillaAnterior+dados) >= tablero.getCantidadCasilleros()) {
                juego.pagarBono(jugador);
                vistaJuego.mostrarMensaje("¡"+jugador.getNombre()+ " has recibido $"+ Constantes.DINERO_VUELTA + " por dar la vuelta al tablero!");
            }
    }

    private void checkDeudaMulta(Jugador jugador) {
        Casillero casilleroDeMulta = tablero.getCasillero(jugador.getUbicacion());
        if(jugador.restarPlata(casilleroDeMulta.getPrecio()) ){
            vistaJuego.mostrarMensaje("Perfecto! El jugador " + jugador.getNombre() + " pudo pagar su multa!");
            jugador.setEstado(Estado.EnJuego);
        }else{
            vistaJuego.mostrarMensaje("EL JUGADOR " + jugador.getNombre() + " NO PAGO SU MULTA! ENTRÓ EN BANCARROTA");
            jugador.setQuiebra();
        }
    }

    private void checkDeudaComprable(Jugador jugador,Propiedad propiedad){
        if (jugador.restarPlata(propiedad.getAlquiler())){
            vistaJuego.mostrarMensaje("Perfecto! El jugador " + jugador.getNombre() + " pudo pagar su deuda!");
            jugador.setEstado(Estado.EnJuego);
        }else{
            vistaJuego.mostrarMensaje("EL JUGADOR " + jugador.getNombre() + " NO PAGO SU DEUDA! ENTRÓ EN BANCARROTA");
            jugador.setQuiebra();
        }
    }

    private void ejecutar(Jugador jugador, int ubicacionJugador){
        CasilleroEjecutable casillero = tablero.getCasilleroEjecutable(ubicacionJugador);
        vistaJuego.mostrarMensaje(casillero.ejecutarCasillero(jugador));
    }

    private int corroboroAccion(String accion) {
        CheckStrToInt checkStrToInt = new CheckStrToInt();
        return checkStrToInt.checkStringToInt(accion);
    }
}





