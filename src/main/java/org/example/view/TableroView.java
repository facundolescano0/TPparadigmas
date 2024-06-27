package org.example.view;

import org.example.funciones.FuncionColorPrints;
import org.example.model.EstacionTransporte;
import org.example.model.Jugador;
import org.example.model.Barrio;
import org.example.model.Propiedad;
import org.example.model.Tablero;
import org.fusesource.jansi.Ansi;
import java.util.ArrayList;
import java.util.HashMap;

public class TableroView {
    private  Tablero tablero;
    private HashMap<Integer, ArrayList<Jugador>> posJugador;

    public TableroView(Tablero tablero){
        this.tablero= tablero;
    }

    public void mostrar(ArrayList<Jugador> jugadores){
        int anchoNumero = String.valueOf(tablero.getCantidadCasilleros()).length();
        int anchoCasillero = 20;
        int anchoPrecio = 10;
        int anchoBarrio = 10;
        int anchoPropietario = 15;
        String formato = "|"+"%-" + anchoNumero + "s" + "|" + "%-" + anchoCasillero + "s" + "|" + "%-" + anchoPrecio + "s" + "|" + "%-" + anchoBarrio + "s" + "|" + "%-" + anchoPropietario + "s" + "|";
        System.out.println(" ");
        System.out.println(String.format(formato, "N°", "CASILLERO", "PRECIO", "BARRIO", "PROPIETARIO") + " JUGADORES");
        this.posJugador= new HashMap<>();
        completarHashPosiciones(jugadores);
        for (int i = 0; i< tablero.getCantidadCasilleros(); i++){
            ArrayList<Jugador> jugadoresEnPosicion = posJugador.get(i);
            if (!(tablero.getCasillero(i).getEsComprable())) {
                System.out.print(String.format(formato, i, tablero.getCasillero(i).getEfecto(), "", "", ""));
            }else {
                if (tablero.getCasillero(i).getEsCasilleroPropiedad()){
                    ArrayList<Barrio> barrios = tablero.getBarrios();
                    imprimirCasillerosPropiedad(barrios,i,formato);
                }else{
                    imprimirCasillerosEstacion(i,formato);
                }
            }
            if (jugadoresEnPosicion!=null){
                imprimirNombresJugadores(jugadoresEnPosicion);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private void imprimirCasillerosEstacion(int numeroDeCasillero, String formato){
        Ansi colorANSI = Ansi.ansi().reset();
        Ansi resetColor = Ansi.ansi().reset();
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        EstacionTransporte propiedad= tablero.getCasillero(numeroDeCasillero).getPropiedad();
        Jugador propietario = propiedad.getPropietario();
        String nombre = "";
        if (propietario != null){
            colorANSI = funcionColorPrints.obtenerColorANSI(propietario.getColor());
            resetColor = Ansi.ansi().reset();
            nombre = propiedad.getNombrePropietario();
        }
        System.out.print(colorANSI+String.format(formato, numeroDeCasillero, tablero.getCasillero(numeroDeCasillero).getEfecto(), "$"+tablero.getCasillero(numeroDeCasillero).getPrecio(), "",(propietario != null ? nombre : "SIN PROPIETARIO"))+resetColor);
    }

    private void imprimirCasillerosPropiedad(ArrayList<Barrio> barrios,int numeroDeCasillero, String formato){
        Ansi colorANSI = Ansi.ansi().reset();
        Ansi resetColor = Ansi.ansi().reset();
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        for (Barrio barrio: barrios){
            for (Propiedad propiedad: barrio.getPropiedades()){
                Jugador propietario = propiedad.getPropietario();
                if (propiedad == tablero.getCasillero(numeroDeCasillero).getPropiedad()){
                    String nombre = "";
                    if (propietario!= null){
                        colorANSI = funcionColorPrints.obtenerColorANSI(propietario.getColor());
                        resetColor = Ansi.ansi().reset();
                        nombre = propiedad.getNombrePropietario();
                    }
                    System.out.print(colorANSI+String.format(formato, numeroDeCasillero, tablero.getCasillero(numeroDeCasillero).getEfecto(), "$" + tablero.getCasillero(numeroDeCasillero).getPrecio(), "BARRIO:" +  barrio.getNumeroBarrio(), (propietario != null ? nombre : "SIN PROPIETARIO")) + resetColor);
                }
            }
        }
    }

    private void completarHashPosiciones(ArrayList<Jugador> jugadores){
        for(Jugador jugador: jugadores){
            if (posJugador.get(jugador.getUbicacion())!=null){
                posJugador.get(jugador.getUbicacion()).add(jugador);
            }else{
                ArrayList<Jugador> listaDeJugador = new ArrayList<>();
                listaDeJugador.add(jugador);
                posJugador.put(jugador.getUbicacion(),listaDeJugador);
            }
        }
    }

    private void imprimirNombresJugadores(ArrayList<Jugador> jugadores){
        Ansi colorANSI = null;
        Ansi resetColor = null;
        FuncionColorPrints funcionColorPrints = new FuncionColorPrints();
        for (Jugador jugador: jugadores){
            colorANSI = funcionColorPrints.obtenerColorANSI(jugador.getColor());
            resetColor = Ansi.ansi().reset();
            System.out.print(" " + colorANSI + jugador.getNombre() + resetColor + " ");
        }
    }


}


