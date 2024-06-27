package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.controller.*;
import org.example.funciones.FuncionesExtras;
import org.example.model.tipoCasilleros.Casillero;
import org.example.model.tipoCasilleros.CasilleroEjecutable;
import org.example.model.tipoCasilleros.DePaso;
import org.example.model.tipoCasilleros.TipoCasillero;
import org.fusesource.jansi.Ansi;

public class Juego {
    private ArrayList<Jugador> jugadores;
    private Tablero tablero;
    private AdministradorTurnos administradorDeTurnos;
    private AdministradorDeMovimientos administradorDeMovimientos;
    private Configuracion configuracion;
    private Banco banco;
    private Suscriptor suscriptor;
    private FachadaAcciones fachada;
    private FuncionesExtras funcionesExtras;
    private ConstruccionController controllConstrucciones;

    public Juego(List<String> configuraciones){
        Configuracion configuracion = new Configuracion(configuraciones);
        this.configuracion = configuracion;
        jugadores = configuracion.getJugadores();
        this.administradorDeTurnos = new AdministradorTurnos(jugadores);
        this.tablero = new Tablero(configuracion);
        this.administradorDeMovimientos = new AdministradorDeMovimientos(this.tablero);
        this.banco = new Banco(configuracion.getMontoVuelta());
        this.funcionesExtras = new FuncionesExtras(tablero);
        this.controllConstrucciones = new ConstruccionController(tablero.getBarrios());
        this.fachada = new FachadaAcciones(new Hipotecar(funcionesExtras),new Comprar(funcionesExtras),new Vender(funcionesExtras),new ConsultarPrecios(funcionesExtras),new Construir(funcionesExtras),new Deshipotecar(funcionesExtras),new PagarFianza());
    }

    public void addObserver(Suscriptor suscriptor){
        this.suscriptor = suscriptor;
    }

    public Jugador getJugadorActual() {
        return administradorDeTurnos.getTurnoActual();
    }

    public Boolean terminado() {
        return checkEstadoJugadores(jugadores);
    }

    public Jugador obtenerGanador(){
        for(Jugador jugador: jugadores){
            if (jugador.getEstado().equals(Estado.Gano)){
                return jugador;
            }
        }
        return jugadores.get(0);
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

    public String pagarBono(Jugador jugador){
        return banco.pagarBono(jugador);
    }

    public boolean checkEstadoJugadores(List<Jugador> jugadores){
        for(Jugador jugador: jugadores){
            if (jugador.getEstado().equals(Estado.Gano)){
                return true;
            }
        }
        return jugadores.size() == 1;
    }
    public void empezarTurno(Jugador jugador, Ansi colorANSI){
        suscriptor.recibirNoticia(jugador.obtenerAccionesDisponibles(colorANSI));
    }
    public void realizarJuego(Jugador jugador, Ansi colorANSI){
        if (jugador.getEstado().equals(Estado.Preso)){
            suscriptor.recibirNoticia(jugarTurnoPreso(jugador,colorANSI));
        }
        else{
            jugarTurnoLibre(jugador,colorANSI);
        }
    }

    private String jugarTurnoPreso(Jugador jugador, Ansi colorANSI){
        Ansi resetColor = Ansi.ansi().reset();
        suscriptor.recibirNoticia(colorANSI + "Es el turno de " + jugador.getNombre() + "\n");
        Scanner scanner = new Scanner(System.in);
        int numeroElecto = -1;
        suscriptor.recibirEstadoJugadores();
        while ((numeroElecto != 0) && (numeroElecto != 1) && (jugador.getCondena() != 0)) {
            suscriptor.recibirNoticia(jugador.obtenerAccionesDisponibles(colorANSI));
            String accion = scanner.nextLine();
            numeroElecto = corroboroAccion(accion);
        }
        if (numeroElecto == -1) {
            jugador.setEstado(Estado.EnJuego);
            jugador.actualizarEstadoAcciones();
            return ("El jugador " + jugador.getNombre() + " complió su condena!");

        }else if(numeroElecto == 1) {
            Accion accionElecta = new Acciones().getAccion(numeroElecto,EstadoAcciones.PRESO);
            return (ejecutarAccion(accionElecta,jugador));

        }else{
            int dados = tirarDados();
            FuncionesExtras.delay(1000);
            String mensaje = ("Saco: " + dados + "\n") ;
            if (dados > jugador.getCondena()) {
                mensaje += (jugador.getNombre() + " queda libre por sacar " + dados + " (dados) mayor que el numero de condena (" + jugador.getCondena() + ")");
                jugador.quedaLibre();
                return mensaje;
            }else {
                jugador.restarCondena();
                mensaje += (jugador.getNombre() + " sigue preso. Ahora su condena es de (" + jugador.getCondena() + ")");
                return mensaje;
            }
        }
    }


    private void jugarTurnoLibre(Jugador jugador, Ansi colorANSI) {
        int dados = tirarDadosYAvanzar(jugador,colorANSI);
        ejecutarCasillero(jugador, dados);
        if (jugador.getEstado() == Estado.Preso) return;
        realizarAcciones(jugador, colorANSI);
        verificarDeuda(jugador);
        verificarEstadoJugador(jugador);
    }

    private int tirarDadosYAvanzar(Jugador jugador, Ansi colorANSI) {
        int dados = tirarDados();
        Ansi resetColor = Ansi.ansi().reset();
        suscriptor.recibirNoticia(colorANSI + "Es el turno de " + jugador.getNombre() + "\n" + "Te toco el numero: " + dados + "\n");
        int casillaAnterior = jugador.getUbicacion();
        int casillaActual = administradorDeMovimientos.avanzarJugador(jugador, dados);
        suscriptor.recibirNoticia("Usted esta en el casillero: " + casillaActual + "\n" + resetColor);
        suscriptor.recibirNoticia(pagarBono(jugador, dados, casillaAnterior));
        return dados;
    }

    private void ejecutarCasillero(Jugador jugador, int dados) {
        Casillero casillero = tablero.getCasillero(jugador.getUbicacion());
        if (casillero.getEsEjecutable()) {
            suscriptor.recibirNoticia(ejecutar(jugador, jugador.getUbicacion()));
        }
    }

    private void realizarAcciones(Jugador jugador, Ansi colorANSI) {
        int numeroElecto = 1;
        suscriptor.recibirEstadoJugadores();
        suscriptor.recibirNoticia(jugador.obtenerAccionesDisponibles(colorANSI));
        Acciones acciones = new Acciones();
        Ansi resetColor = Ansi.ansi().reset();
        acciones.acciones(colorANSI, resetColor);
        Scanner scanner = new Scanner(System.in);
        while (numeroElecto != 0) {
            suscriptor.recibirNoticia(colorANSI + "Seleccione la accion que quiere realizar indicando su numero (NUMERO):" + resetColor);
            String accion = scanner.nextLine();
            numeroElecto = corroboroAccion(accion);
            if (numeroElecto == Constantes.NEGATIVO) {
                suscriptor.recibirNoticia("Accion inexistente\n");
            } else {
                Accion accionElecta = acciones.getAccion(numeroElecto, jugador.getEstadoAcciones());
                if (accionElecta == null || accionElecta == Accion.PAGAR_FIANZA || accionElecta == Accion.TIRAR_DADOS) {
                    suscriptor.recibirNoticia("Accion inexistente\n");
                }else{
                    suscriptor.recibirNoticia(ejecutarAccion(accionElecta, jugador));
                }
            }
        }
    }

    private void verificarDeuda(Jugador jugador) {
        if (jugador.estaEnDeuda()) {
            int ubicacion = jugador.getUbicacion();
            if (tablero.getCasillero(ubicacion).getTipo() == TipoCasillero.MULTA){
                suscriptor.recibirNoticia(checkDeuda(jugador,tablero.getCasillero(ubicacion).getPrecio() , "multa"));
            }else {
                Propiedad propiedad = tablero.getPropiedad(ubicacion).getPropiedad();
                suscriptor.recibirNoticia(checkDeuda(jugador, propiedad.getAlquiler(), "alquiler"));
            }
        }
    }

    private void verificarEstadoJugador(Jugador jugador) {
        if (jugador.estaEnQuiebra()) {
            suscriptor.recibirNoticia(jugador.getNombre() + " perdio!\n");
            eliminarPropiedadesDelJugadorEnQuiebra(jugador);
            eliminarJugador(jugador);
            terminado();
        }
        if (ComprobarGanarJugador(jugador)){
            terminado();
        }
    }

    private String ejecutarAccion(Accion accionElecta, Jugador jugador) {
        if(accionElecta == Accion.TERMINAR_TURNO){
            return "";
        }
        else if (accionElecta == Accion.COMPRAR){
            return fachada.comprar(jugador,0,controllConstrucciones);
        }else if (accionElecta != Accion.PAGAR_FIANZA){
            CheckStrToInt checkStrToInt = new CheckStrToInt();
            Scanner scanner = new Scanner(System.in);
            suscriptor.recibirNoticia("Seleccione el casillero en que se encuentra la propiedad (NUMERO):");
            String casillero = scanner.nextLine();
            int numero = (checkStrToInt.checkStringToInt(casillero));
            switch (accionElecta) {
                case CONSTRUIR -> {
                    return(fachada.construir(jugador, numero, controllConstrucciones));
                }
                case VENDER -> {
                    return fachada.vender(jugador, numero, controllConstrucciones);
                }
                case HIPOTECAR -> {
                    return fachada.hipotecar(jugador, numero, controllConstrucciones);
                }
                case DESHIPOTECAR -> {
                    return fachada.deshipotecar(jugador, numero, controllConstrucciones);
                }
                case CONSULTAR_PRECIO_CASA -> {
                    return fachada.consultar_precio_casa(jugador, numero, controllConstrucciones);
                }
                default -> {
                    return "Accion inexistente";
                }
            }
        }
        else{
            return fachada.pagar_fianza(jugador, tablero.getCarcel());
        }
    }

    private String pagarBono(Jugador jugador,int dados,int casillaAnterior){
        if((casillaAnterior+dados) >= tablero.getCantidadCasilleros()) {
            return pagarBono(jugador);
        }else{
            return "";
        }
    }

    private String checkDeuda(Jugador jugador, double montoDeuda, String tipoDeuda) {
        if (jugador.restarPlata(montoDeuda)) {
            jugador.setEstado(Estado.EnJuego);
            return "Perfecto! El jugador " + jugador.getNombre() + " pudo pagar su " + tipoDeuda + "!";
        } else {
            jugador.setQuiebra();
            return "EL JUGADOR " + jugador.getNombre() + " NO PAGO SU " + tipoDeuda + "! ENTRÓ EN BANCARROTA";
        }
    }

    private String ejecutar(Jugador jugador, int ubicacionJugador){
        CasilleroEjecutable casillero = tablero.getCasilleroEjecutable(ubicacionJugador);
        return casillero.ejecutarCasillero(jugador);
    }

    private int corroboroAccion(String accion) {
        CheckStrToInt checkStrToInt = new CheckStrToInt();
        return checkStrToInt.checkStringToInt(accion);
    }
    public void eliminarPropiedadesDelJugadorEnQuiebra(Jugador jugador){
        ArrayList<Propiedad> propiedades = jugador.getPropiedades();
        for (Propiedad propiedad : propiedades) {
            propiedad.setPropietario(null);
            int ubicacion = propiedad.getUbicacion();
            DePaso NuevoCasilleroDePaso = new DePaso(ubicacion);
            tablero.getTodosLosCasilleros()[ubicacion] =   NuevoCasilleroDePaso;
        }
    }

    public boolean ComprobarGanarJugador(Jugador jugador) {
        ArrayList<Barrio> barrios = tablero.getBarrios();
        ArrayList<Propiedad> propiedadesJugador = jugador.getPropiedades();
        for (Barrio barrio : barrios) {
            ArrayList<Propiedad> propiedadesBarrio = barrio.getPropiedades();
            boolean todasPropiedadesPertenecenAJugador = propiedadesJugador.containsAll(propiedadesBarrio);
            if (todasPropiedadesPertenecenAJugador) {
                boolean todasPropiedadesEnHotel = true;
                for (Propiedad propiedad : propiedadesBarrio) {
                    if (propiedad.getConstrucciones() != Construcciones.HOTEL) {
                        todasPropiedadesEnHotel = false;
                        break;
                    }
                }
                if (todasPropiedadesEnHotel) {
                    jugador.setEstado(Estado.Gano);
                    return true;
                }
            }
        }
        return false;
    }
}