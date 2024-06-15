package org.example.controller;


import org.example.model.Colores;
import org.example.model.Jugador;
import org.fusesource.jansi.Ansi;
import org.example.funciones.FuncionColorPrints;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Configuracion {
    private int montoVuelta;
    private int montoInicial;
    private int cantidadDeLados;
    private int condenaCarcel;
    private final int montoDeMulta;
    private int cantidadCasilleros;
    private int montoFianza;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Colores> colores;


    public Configuracion(List<String> configuraciones) {
        cantidadCasilleros = Constantes.CANTIDAD_CASILLEROS;
        montoInicial = Constantes.DINERO_INICIAL;
        cantidadDeLados= Constantes.CANTIDAD_DE_LADOS_EN_DADO;
        montoVuelta = Constantes.DINERO_VUELTA;
        condenaCarcel = Constantes.TURNOS_PRESO;
        montoDeMulta = Constantes.MULTA;
        montoFianza = Constantes.FIANZA;

        ArrayList<Jugador> jugadores = this.crearJugadores(configuraciones);
        this.jugadores = jugadores;
        asignacionColores(jugadores);
    }

    private ArrayList<Jugador> crearJugadores(List<String> configuraciones){
        List<String> nombres = List.of(configuraciones.get(0).split(" "));
        int plataInicial = Constantes.DINERO_INICIAL;
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        for (String nombre : nombres) {
            Jugador jugador = new Jugador(nombre);
            jugador.setPlata(plataInicial);
            jugadores.add(jugador);
        }

        return jugadores;
    }

    private void asignacionColores(List<Jugador> jugadores){
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        Colores.Color[] colores = Colores.Color.values();
        for(int i = 0; i < jugadores.size(); i++){
            System.out.println("El jugador " + jugadores.get(i).getNombre() + " debe elegir el color: ");
            int opcionColor=this.elegirColor(colores);
            jugadores.get(i).setColor(colores[opcionColor-1]);
            colores[opcionColor -1] = null;
        }
    }

    private int validarIngresoColor(Colores.Color[] colores, int opcion){
        while(opcion <1 || opcion > colores.length || colores[opcion-1] ==null){
            System.out.println("ERROR: NUMERO NO VALIDO");
            opcion = elegirColor(colores);
        }
        return opcion;
    }

    private int elegirColor(Colores.Color[] colores ){
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        System.out.println("Los colores disponibles son:");
        for (int i= 0; i < colores.length; i++){
            if (colores[i]!= null){
                Ansi colorANSI = funcionColorPrints.obtenerColorANSI(colores[i]);
                Ansi resetColor = Ansi.ansi().reset();
                System.out.println(i+1 +") Color: " + colorANSI+ colores[i] + resetColor);

            }
        }
        int opcionInt = 1;
        boolean falla = true;
        while (falla) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Ingrese el número de color que desea elegir: ");
                String opcion = scanner.nextLine();
                opcionInt = Integer.parseInt(opcion);
                opcionInt = validarIngresoColor(colores, opcionInt);
                falla = false;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número.");
            }
        }
        return opcionInt;
    }

    public double getMontoVuelta() {
        return montoVuelta;
    }

    public double getMontoInicial(){
        return (double) montoInicial;
    }

    public ArrayList<Jugador> getJugadores() { return jugadores;}

    public double getMontoFianza() { return  montoFianza; }

    public int getCantidadCasilleros() { return cantidadCasilleros; }

    public int getCondenaCarcel(){return condenaCarcel;}

    public double getMontoMulta(){return montoDeMulta;}

    public int getCantidadDeLadosEnDado(){ return cantidadDeLados;}
}
