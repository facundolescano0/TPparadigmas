package org.example.model;

import org.example.controller.Constantes;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Jugador{
    private final String nombre;
    private  Colores.Color color;
    private double plata;
    private int ubicacion;
    private ArrayList<EstacionTransporte> estaciones;
    private ArrayList<Propiedad> propiedades;
    private Estado estado;
    private int condena;
    private double patrimonio;
    private EstadoAcciones estadoAcciones;
    private Acciones acciones;
    private String textoAcciones;
    private boolean estaJugando;

    public Jugador(String nombre) {
        this.ubicacion = 0;
        this.nombre = nombre;
        this.estado = Estado.EnJuego;
        this.propiedades = new ArrayList<>();
        this.condena = 0;
        this.estaciones = new ArrayList<>();
        this.patrimonio = 0;
        this.estadoAcciones = EstadoAcciones.SIN_PROPIEADES;
        this.acciones = new Acciones();
        this.textoAcciones= "";
        this.estaJugando = false;

    }

    public void sumarAlPatrimonio(double monto){
        this.patrimonio += monto;
    }

    public void agregarPropiedad(Propiedad propiedad){
        propiedades.add(propiedad);
    }

    public void eliminarPropiedad(Propiedad propiedad){
        propiedades.remove(propiedad);
    }

    public void agregarEstacion(EstacionTransporte estacion){estaciones.add(estacion);}

    public String hipotecarPropiedad(Barrio barrio,Propiedad propiedad){
        return propiedad.hipotecar(barrio,this);

    }

    public void restarPatrimonio(double monto){
        this.patrimonio -= monto;
    }

    public ArrayList<EstacionTransporte> getEstaciones() {
        return estaciones;
    }

    public String deshipotecarPropiedad(Propiedad propiedad){
        return propiedad.deshipotecar(this);
    }

    public void agregarComprable(Comprable comprable){
        if(comprable.getEsPropiedad()){
            agregarPropiedad((Propiedad) comprable);
        }else {
            agregarEstacion((EstacionTransporte) comprable);
        }
    }

    public void eliminarComprable(Comprable comprable){
        if(comprable.getEsPropiedad()) {
            eliminarPropiedad((Propiedad)comprable);
        }else{
            eliminarEstacion((EstacionTransporte)comprable);
        }
    }

    public String comprarComprable(Comprable comprable){
        double precioComprable = comprable.getPrecio();
        if (this.plata >= precioComprable) {
            sumarAlPatrimonio(precioComprable * Constantes.PORCENTAJE_DE_VENTA);
            comprable.setPropietario(this);
            actualizarEstadoAcciones();
            return "Se ha comprado la propiedad";
        }else{
            return "No se puede comprar propiedad";
        }
    }

    public void setQuiebra(){estado = Estado.Quiebra;}

    public void setDeuda(){estado = Estado.EnDeuda;}

    public void setPlata(double plata) {
        this.plata = plata;
    }

    public void setColor(Colores.Color color) {
        this.color = color;
    }

    public void setEstado(Estado nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void setCondena(int condena){this.condena = condena;}

    public String getNombre() {return this.nombre;}

    public Estado getEstado(){
        return this.estado;
    }
    public double getPlata() {return plata;}

    public int getUbicacion() {return ubicacion;}

    public int getCondena(){
        return this.condena;
    }
    public Colores.Color getColor() {return this.color;}

    public ArrayList<Propiedad> getPropiedades(){return this.propiedades;}

    public boolean restarPlata(double dinero) {
        if (plata >= dinero) {
            plata -= dinero;
            return true;
        } else {
            return false;
        }
    }

    public void restarCondena(){
        this.condena--;
    }

    public void sumarPlata(double dinero){this.plata += dinero;}

    public void setUbicacion(int ubicacion){ this.ubicacion = ubicacion; }

    public boolean estaEnQuiebra(){return Estado.Quiebra.equals(this.estado);}

    public boolean estaEnDeuda(){return Estado.EnDeuda.equals(this.estado);}

    public double getPatrimonioTotal(){
        return patrimonio + plata;
    }

    public void quedaLibre(){
        this.setCondena(0);
        this.estado = Estado.EnJuego;
    }

    public void eliminarEstacion(EstacionTransporte estacionTransporte) {
        estaciones.remove(estacionTransporte);
    }

    public String venderEstacion(Comprable comprable){
        eliminarComprable(comprable);
        restarPatrimonio(comprable.getPrecio());
        return comprable.venderComprable();
    }


    public EstadoAcciones getEstadoAcciones(){
        return estadoAcciones;
    }
    public void setEstadoAcciones(EstadoAcciones estadoAcciones){
        this.estadoAcciones = estadoAcciones;
    }

    public void actualizarEstadoAcciones(){
        if (estado != Estado.Preso){
            this.estadoAcciones = EstadoAcciones.PRESO;
            if (!propiedades.isEmpty() || !estaciones.isEmpty()){
                if (tieneBarrio()) {
                    this.estadoAcciones = EstadoAcciones.CON_BARRIO;
                }else{
                    this.estadoAcciones = EstadoAcciones.CON_CASA;
                }
            }else{
                this.estadoAcciones = EstadoAcciones.SIN_PROPIEADES;
            }
        }
    }
    private boolean tieneBarrio(){
        Map<Integer, Integer> ocurrencias = new HashMap<>();
        for (Propiedad propiedad : propiedades){
            if (ocurrencias.containsKey(propiedad.getBarrio())){
                ocurrencias.put(propiedad.getBarrio(), ocurrencias.get(propiedad.getBarrio()) + 1);
            }else{
                ocurrencias.put(propiedad.getBarrio(), 1);
            }
        }
        for (Map.Entry<Integer, Integer> entry : ocurrencias.entrySet()){
            if (entry.getValue() == Constantes.CANTIDAD_CASAS_POR_BARRIO){
                return true;
            }
        }
        return false;
    }

    public String obtenerAccionesDisponibles(Ansi colorANSI){
        actualizarEstadoAcciones();
        String mensaje = "";
        Ansi resetColor = Ansi.ansi().reset();
        switch (estadoAcciones) {
            case CON_BARRIO:
                mensaje = acciones.accionesJugadorConBarrio(colorANSI, resetColor);
                break;
            case CON_CASA:
                mensaje = acciones.accionesJugadorConPropiedad(colorANSI, resetColor);
                break;
            case PRESO:
                mensaje = acciones.accionesJugadorPreso(colorANSI, resetColor);
                break;
            case SIN_PROPIEADES:
                mensaje = acciones.accionesJugadorSinPropiedad(colorANSI, resetColor);
                break;
            default:
                break;
        }
        return mensaje;
    }

}