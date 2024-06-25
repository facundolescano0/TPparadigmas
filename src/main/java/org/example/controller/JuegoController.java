package org.example.controller;

import org.example.funciones.FuncionColorPrints;
import org.example.funciones.FuncionesExtras;
import org.example.model.*;
import org.example.model.tipoCasilleros.*;
import org.example.view.JugadorView;
import org.example.view.TableroView;
import org.fusesource.jansi.Ansi;
import org.example.view.JuegoView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.example.model.Accion;

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
    private CheckGanarJugador checkGanarJugador;


    public JuegoController(Juego juego) throws IOException {
        this.juego = juego;
        this.tablero = juego.getTablero();
        this.vistaJuego = new JuegoView(juego);
        this.tableroView = new TableroView(tablero);

        this.controllConstrucciones = new ConstruccionController(tablero.getBarrios());
        this.funcionesExtras = new FuncionesExtras(tablero);
        this.jugadorView = new JugadorView(juego.getJugadores());
        //this.checkGanarJugador= new CheckGanarJugador(tablero);
        this.fachada = new FachadaAcciones(new Hipotecar(funcionesExtras),new Comprar(funcionesExtras),new Vender(funcionesExtras),new ConsultarPrecios(funcionesExtras),new Construir(funcionesExtras),new Deshipotecar(funcionesExtras),new PagarFianza());
    }


    public void jugarTurno() throws IOException {
        juego.cambiarTurno();
        Jugador jugador = juego.getJugadorActual();
        Ansi colorANSI = null;
        Ansi resetColor = Ansi.ansi().reset();
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        colorANSI = funcionColorPrints.obtenerColorANSI(jugador.getColor());

        juego.realizarJuego(jugador,colorANSI);
        //juego.empezarTurno(jugador, colorANSI);
    }


/*
    public JuegoController(Juego juego) throws IOException {
        this.juego = juego;
        this.tablero = juego.getTablero();
        this.vistaJuego = new JuegoView(juego);
        this.controlTablero = new TableroController(tablero);
        this.administradorDeMovimientos = new AdministradorDeMovimientos(juego.getTablero());
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
            juego.eliminarJugador(jugador);
            juego.cambiarTurno();

        }
    }*/

    @Override
    public void recibirNoticia(String mensaje) {
        vistaJuego.imprimirMensajes(mensaje);
    }

    @Override
    public void recibirEstadoJugadores() {
        vistaJuego.mostrar();
    }

    /*
    private void jugarTurnoPreso(Jugador jugador){
        Ansi colorANSI = null;
        Ansi resetColor = Ansi.ansi().reset();
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        colorANSI = funcionColorPrints.obtenerColorANSI(jugador.getColor());
        System.out.println(colorANSI+"Es el turno de " + jugador.getNombre() + "\n");

        Acciones acciones = new Acciones();
        acciones.accionesJugadorPreso(colorANSI,resetColor);
        Scanner scanner = new Scanner(System.in);
        int numeroElecto = -1;
        while((numeroElecto != 0) && (numeroElecto != 1) && (jugador.getCondena() != 0)){
            System.out.println("Seleccione la accion que quiere realizar indicando su numero (NUMERO):\n");
            String accion = scanner.nextLine();
            numeroElecto = corroboroAccion(accion);
        }
        if (numeroElecto == -1){
            FuncionesExtras.delay(1000);
            System.out.println("El jugador "+ jugador.getNombre() + " complió su condena!");
            FuncionesExtras.delay(1000);
            jugador.setEstado(Estado.EnJuego);

        } else if(numeroElecto == 1){
            Accion accionElecta = acciones.getAccionPreso(numeroElecto);
            ejecutarAccion(accionElecta,jugador);
        }else{
            int dados = juego.tirarDados();
            FuncionesExtras.delay(1000);
            System.out.println("Saco: " + dados);
            FuncionesExtras.delay(1500);
            if(dados > jugador.getCondena()){
                jugador.quedaLibre();
                System.out.println(jugador.getNombre()+ " queda libre por sacar "+ dados+  " (dados) mayor que el numero de condena ("+ jugador.getCondena()+")");
            } else {
                jugador.restarCondena();
                System.out.println(jugador.getNombre()+ " sigue preso. Ahora su condena es de ("+ jugador.getCondena()+")");
            }
            FuncionesExtras.delay(1000);
        }
        if(jugador.getEstado() == Estado.EnJuego){
            jugarTurnoLibre(jugador);
        }
    }


    private void jugarTurnoLibre(Jugador jugador) {
        int dados = juego.tirarDados();
        Ansi colorANSI = null;
        Ansi resetColor = Ansi.ansi().reset();
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        colorANSI = funcionColorPrints.obtenerColorANSI(jugador.getColor());
        System.out.println(colorANSI + "Es el turno de " + jugador.getNombre() + "\n" + "Te toco el numero: " + dados + "\n");
        int casillaAnterior = jugador.getUbicacion();
        int casillaActual = administradorDeMovimientos.avanzarJugador(jugador, dados);
        System.out.println("Usted esta en el casillero: " + casillaActual + "\n" + resetColor);
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
            System.out.println(colorANSI + "Seleccione la accion que quiere realizar indicando su numero (NUMERO):\n"+resetColor);
            String accion = scanner.nextLine();
            numeroElecto = corroboroAccion(accion);
            if (numeroElecto == Constantes.NEGATIVO) {
                System.out.println("Accion inexistente\n");
            } else {
                Accion accionElecta = acciones.getAccion(numeroElecto);
                if (accionElecta == null || accionElecta == Accion.PAGAR_FIANZA || accionElecta == Accion.TIRAR_DADOS) {
                    System.out.println("Accion inexistente");
                }else{
                    ejecutarAccion(accionElecta, jugador);
                }
            }
        }
        if (jugador.estaEnDeuda()) {
            int ubicacion = jugador.getUbicacion();
            if (tablero.getCasillero(ubicacion).getTipo() == TipoCasillero.MULTA){
                checkDeudaMulta(jugador);
            }else {
                Propiedad propiedad = tablero.getPropiedad(ubicacion).getPropiedad();
                checkDeudaComprable(jugador, propiedad);
            }
        }
        if (jugador.estaEnQuiebra()) {
            System.out.printf("%s perdio!\n", jugador.getNombre());
            controlTablero.eliminarPropiedadesDelJugadorEnQuiebra(jugador);
            juego.eliminarJugador(jugador);
            juego.terminado();
        }
        if (checkGanarJugador.ComprobarGanarJugador(jugador)){
            juego.terminado();
        }
    }

    private void ejecutarAccion(Accion accionElecta, Jugador jugador) {
        if (accionElecta == Accion.COMPRAR){
            fachada.comprar(jugador,0,controllConstrucciones);
        }else if (accionElecta != Accion.TERMINAR_TURNO && accionElecta != Accion.PAGAR_FIANZA){
            CheckStrToInt checkStrToInt = new CheckStrToInt();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Seleccione el casillero en que se encuentra la propiedad (NUMERO):");
            String casillero = scanner.nextLine();
            int numero = (checkStrToInt.checkStringToInt(casillero));
            switch (accionElecta) {
                case CONSTRUIR -> fachada.construir(jugador, numero, controllConstrucciones);
                case VENDER -> fachada.vender(jugador, numero, controllConstrucciones);
                case HIPOTECAR -> fachada.hipotecar(jugador, numero, controllConstrucciones);
                case DESHIPOTECAR -> fachada.deshipotecar(jugador, numero, controllConstrucciones);
                case CONSULTAR_PRECIO_CASA -> fachada.consultar_precio_casa(jugador, numero, controllConstrucciones);
            }
        }
        if (accionElecta == Accion.PAGAR_FIANZA){
            fachada.pagar_fianza(jugador, tablero.getCarcel());
        }
    }

    private void pagarBono(Jugador jugador,int dados,int casillaAnterior){
        if((casillaAnterior+dados) >= tablero.getCantidadCasilleros()) {
            juego.pagarBono(jugador);
        }
    }

    private void checkDeudaMulta(Jugador jugador) {
        Casillero casilleroDeMulta = tablero.getCasillero(jugador.getUbicacion());
        if(jugador.restarPlata(casilleroDeMulta.getPrecio()) ){
            System.out.println("Perfecto! El jugador "+jugador.getNombre() +" pudo pagar su multa!");
            jugador.setEstado(Estado.EnJuego);
        }else{
            System.out.println("EL JUGADOR "+ jugador.getNombre()+" NO PAGO SU MULTA! ENTRÓ EN BANCARROTA");
            jugador.setQuiebra();
        }
    }

    private void checkDeudaComprable(Jugador jugador,Propiedad propiedad){
        if (jugador.restarPlata(propiedad.getAlquiler())){
            System.out.println("Perfecto! El jugador "+jugador.getNombre() +" pudo pagar su deuda!");
            jugador.setEstado(Estado.EnJuego);
        }else{
            System.out.println("EL JUGADOR "+ jugador.getNombre()+" NO PAGO SU DEUDA! ENTRÓ EN BANCARROTA");
            jugador.setQuiebra();
        }
    }

    private void ejecutar(Jugador jugador, int ubicacionJugador){
        CasilleroEjecutable casillero = tablero.getCasilleroEjecutable(ubicacionJugador);
        casillero.ejecutarCasillero(jugador);
    }

    private int corroboroAccion(String accion) {
        CheckStrToInt checkStrToInt = new CheckStrToInt();
        return checkStrToInt.checkStringToInt(accion);
    }*/

}




