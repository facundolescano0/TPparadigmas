package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.controller.*;
import org.example.funciones.FuncionesExtras;
import org.example.funciones.FuncionColorPrints;
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
    private FachadaAcciones fachada;
    private FuncionesExtras funcionesExtras;
    private ConstruccionController construccionesController;


    public Juego(List<String> configuraciones){
        Configuracion configuracion = new Configuracion(configuraciones);
        this.configuracion = configuracion;
        jugadores = configuracion.getJugadores();
        this.administradorDeTurnos = new AdministradorTurnos(jugadores);
        this.tablero = new Tablero(configuracion);
        this.banco = new Banco(configuracion.getMontoVuelta());
        this.administradorDeMovimientos = new AdministradorDeMovimientos(tablero);
        this.funcionesExtras = new FuncionesExtras(tablero);
        this.construccionesController = new ConstruccionController(tablero.getBarrios());
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
        String acciones = jugador.obtenerAccionesDisponibles(colorANSI);
        return acciones;
    }

    //devuelve la casilla actual del jugador
    public int moverJugador(Jugador jugador, int dados){
        //----------------------------------------------------------------
        Ansi colorANSI = null;
        Ansi resetColor = Ansi.ansi().reset();
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        colorANSI = funcionColorPrints.obtenerColorANSI(jugador.getColor());
        //----------------------------------------------------------------

        //vistaJuego.mostrarTurnoLibre(jugador.getNombre(), dados, colorANSI, resetColor);
        int casillaActual = administradorDeMovimientos.avanzarJugador(jugador, dados);
        //vistaJuego.mostrarUbicacion(casillaActual, resetColor);
        pagarBono(jugador);
        return casillaActual;
    }
    public String ejecutarCasillero(Jugador jugador,int casillaActual){
        Casillero casillero = tablero.getCasillero(casillaActual);
        if (casillero.getEsEjecutable()) {
            CasilleroEjecutable casilla = tablero.getCasilleroEjecutable(casillaActual);
            return (casilla.ejecutarCasillero(jugador) + "ejecutar casillero");
        }
        return "ejecutar casillero";
    }

//    private void ejecutar(Jugador jugador, int ubicacionJugador){
//        CasilleroEjecutable casillero = tablero.getCasilleroEjecutable(ubicacionJugador);
//        //vistaJuego.mostrarMensaje(casillero.ejecutarCasillero(jugador));
//    }

    public String evaluarAcciones(Jugador jugador){
        Scanner scanner = new Scanner(System.in);
        int numeroElecto = 1;
        while (numeroElecto != 0) {
            String accion = scanner.nextLine();
            numeroElecto = corroboroAccion(accion);
            String accionesDisponibles = empezarTurno(jugador);
            Acciones acciones = new Acciones();
            if (numeroElecto != Constantes.NEGATIVO) {
                Accion accionElecta = acciones.getAccion(numeroElecto);
                if(accionElecta == null || accionElecta == Accion.PAGAR_FIANZA || accionElecta == Accion.TIRAR_DADOS) {
                    //vistaJuego.mostrarAccionInexistente();
                } else{
                    return ejecutarAccion(accionElecta, jugador);
                }
            }
        }
        if (jugador.estaEnDeuda()) {
            int ubicacion = jugador.getUbicacion();
            if(tablero.getCasillero(ubicacion).getTipo() == TipoCasillero.MULTA) {
                return checkDeudaMulta(jugador);
            }else {
                Propiedad propiedad = tablero.getPropiedad(ubicacion).getPropiedad();
                return checkDeudaComprable(jugador, propiedad);
            }
        }else if (jugador.estaEnQuiebra()){
            String mensaje = (jugador.getNombre() + " perdio!\n");
            eliminarPropiedadesDelJugadorEnQuiebra(jugador);
            eliminarJugador(jugador);
            return  mensaje;
        }else if (ComprobarGanarJugador(jugador)) {
            return("\t\t¡¡FELICITACIONES!!\nEl jugador "+ jugador.getNombre() + "ha completado todo un barrio con hoteles. Por eso, HA GANADO");
        }
        return null;
    }



    public String realizarJuego(Jugador jugador){


        if (jugador.getEstado().equals(Estado.Preso)){
            return juegoDePreso(jugador);
        }
        else{
            return  juegoDeLibre(jugador);
        }
    }

    public String juegoDeLibre(Jugador jugador){
        int dados = tirarDados();
        int casillaActual = moverJugador(jugador,dados);
        String mensaje = ejecutarCasillero(jugador,casillaActual);
        //String accionesDisponibles = empezarTurno(jugador);
        //Acciones acciones = new Acciones(); // ENLAZAR CON ACCIONES DISPONIBLES
        //acciones.accionesDisponibles(colorANSI, resetColor,jugador.getEstadoAcciones
        return mensaje + ("Seleccione la accion que quiere realizar indicando su numero (NUMERO):\n") + evaluarAcciones(jugador);
    }

    private String juegoDePreso(Jugador jugador){
        Scanner scanner = new Scanner(System.in);
        int numeroElecto = -1;
        while ((numeroElecto != 0) && (numeroElecto != 1) && (jugador.getCondena() != 0)) {
            String accion = scanner.nextLine();
            numeroElecto = corroboroAccion(accion);
        }
        if (numeroElecto == -1) {
            jugador.setEstado(Estado.EnJuego);
            jugador.actualizarEstadoAcciones();
            return ("El jugador " + jugador.getNombre() + " complió su condena!");

        }else if(numeroElecto == 1) {
            Accion accionElecta = new Acciones().getAccionPreso(numeroElecto);
            return ejecutarAccion(accionElecta, jugador);
        }else{
            int dados = tirarDados();
            FuncionesExtras.delay(1000);
            String mensaje = ("Saco: " + dados + "\n") ;
            if (dados > jugador.getCondena()) {
                jugador.quedaLibre();
               mensaje += (jugador.getNombre() + " queda libre por sacar " + dados + " (dados) mayor que el numero de condena (" + jugador.getCondena() + ")");
               return mensaje;
            }else {
                jugador.restarCondena();
                mensaje += (jugador.getNombre() + " sigue preso. Ahora su condena es de (" + jugador.getCondena() + ")");
                return mensaje;
            }
        }
    }

    private int corroboroAccion(String accion) {
        CheckStrToInt checkStrToInt = new CheckStrToInt();
        return checkStrToInt.checkStringToInt(accion);
    }


    private String ejecutarAccion(Accion accionElecta, Jugador jugador) {
        if (accionElecta == Accion.COMPRAR){
            return (fachada.comprar(jugador,0,construccionesController));
        }else if (accionElecta != Accion.TERMINAR_TURNO && accionElecta != Accion.PAGAR_FIANZA){
            CheckStrToInt checkStrToInt = new CheckStrToInt();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Seleccione el casillero en que se encuentra la propiedad (NUMERO):");
            String casillero = scanner.nextLine();
            int numero = (checkStrToInt.checkStringToInt(casillero));
            switch (accionElecta) {
                case CONSTRUIR -> {
                    return (fachada.construir(jugador, numero, construccionesController));
                }
                case VENDER -> {
                    return (fachada.vender(jugador, numero, construccionesController));
                }
                case HIPOTECAR -> {
                    return (fachada.hipotecar(jugador, numero, construccionesController));
                }
                case DESHIPOTECAR -> {
                    return (fachada.deshipotecar(jugador, numero, construccionesController));
                }
                case CONSULTAR_PRECIO_CASA -> {
                    return (fachada.consultar_precio_casa(jugador, numero, construccionesController));
                }
            }
        }
        else if (accionElecta == Accion.PAGAR_FIANZA){
            return(fachada.pagar_fianza(jugador, tablero.getCarcel()));
        }
        return null;
    }

        private String checkDeudaMulta(Jugador jugador) {
        Casillero casilleroDeMulta = tablero.getCasillero(jugador.getUbicacion());
        if(jugador.restarPlata(casilleroDeMulta.getPrecio()) ){
            jugador.setEstado(Estado.EnJuego);
            return ("Perfecto! El jugador " + jugador.getNombre() + " pudo pagar su multa!");

        }else{
            jugador.setQuiebra();
            return ("EL JUGADOR " + jugador.getNombre() + " NO PAGO SU MULTA! ENTRÓ EN BANCARROTA");

        }
    }

    private String checkDeudaComprable(Jugador jugador,Propiedad propiedad){
        if (jugador.restarPlata(propiedad.getAlquiler())){
            jugador.setEstado(Estado.EnJuego);
            return ("Perfecto! El jugador " + jugador.getNombre() + " pudo pagar su deuda!");

        }else{
            jugador.setQuiebra();
            return ("EL JUGADOR " + jugador.getNombre() + " NO PAGO SU DEUDA! ENTRÓ EN BANCARROTA");

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

    public void eliminarPropiedadesDelJugadorEnQuiebra(Jugador jugador){
        ArrayList<Propiedad> propiedades = jugador.getPropiedades();
        for (Propiedad propiedad : propiedades) {
            propiedad.setPropietario(null);
            int ubicacion = propiedad.getUbicacion();
            DePaso NuevoCasilleroDePaso = new DePaso(ubicacion);
            tablero.getTodosLosCasilleros()[ubicacion] =   NuevoCasilleroDePaso;
        }
    }

}
