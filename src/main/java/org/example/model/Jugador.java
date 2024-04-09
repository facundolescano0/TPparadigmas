package org.example.model;
import org.example.model.Tablero;
import org.example.model.Colores;
import org.example.model.Banco;

import java.util.List;
public class Jugador{
    private final String nombre;
    private final Colores.Color color;
    private int plata;
    private int ubicacion;
    private List<Propiedad> propiedades;
    private Estado estado;
    private int condena;

    public enum Estado{
        EnJuego,Preso,Deuda,Perdio
    }
    public Jugador(String nombre, Colores.Color color, int plata, int ubicacion, List<Propiedad> propiedades) {
        this.nombre = nombre;
        this.color = color;
        this.plata = plata;
        this.ubicacion = ubicacion;
        this.propiedades = propiedades;
        this.estado = Estado.EnJuego;
    }
    public String getNombre() { return this.nombre; }

    public void setEstado(Estado nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public Estado getEstado(){
        return this.estado;
    }

    public int getPlata() {
        return plata;
    }

    public int getUbicacion() {
        return ubicacion;
    }

    public int getCondena(){
        return this.condena;
    }

    public Colores.Color getColor() {
        return this.color;
    }

    public void setPlata(int plata) {
        this.plata = plata;
    }

    public void setCondena(int condena){this.condena += condena;}

    public boolean restarPlata(int dinero){
        if (this.plata > dinero){
            this.plata -= dinero;
            return true;
        }
        System.out.println("Ups!" + this.getNombre() + "no tiene dinero suficiente para pagar esta deuda");
        //Agregar parte de Controller config
        return false;
    }
    public void sumarPlata(int dinero){
        this.plata += dinero;
    }

    public void setUbicacion(int ubicacion){ this.ubicacion = ubicacion; }

    public boolean estaEnQuiebra(Jugador jugador){
        return Estado.Deuda.equals(jugador.getEstado());
    }

    // antes de llamar este metodo ya habria que chequear si esta en quiebra para hacerlo perder
    public void perder(Jugador jugador, List<Jugador> jugadores){

        for (int i=0; i < jugador.propiedades.size(); i++){
            Propiedad propiedad = jugador.propiedades.get(i);
            //propiedad.liberarPropiedad(jugador);
        }
        jugador.setEstado(Estado.Perdio);
        //jugadores.remove(jugador); // faq: hace falta? para mi lo solucionamos con ignorarlo en los turnos
    }

}

