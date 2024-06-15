package org.example.model;

import org.example.controller.Constantes;

public abstract class Comprable {

    protected double precio;
    protected double alquiler;
    protected Jugador propietario;
    protected EstadoPropiedades estado;
    protected int ubicacion;
    protected boolean EsPropiedad;

    public Comprable(double precio, int ubicacion,boolean condicion) {
        this.precio = precio;
        this.ubicacion = ubicacion;
        this.propietario = null;
        this.estado = EstadoPropiedades.EN_VENTA;
        this.alquiler = (precio * Constantes.PORCENTAJE_ALQUILER);
        this.EsPropiedad = condicion;
    }

    public boolean getEsPropiedad(){
        return EsPropiedad;
    }

    public EstadoPropiedades getEstado() {
        return this.estado;
    }

    public double getPrecio() { return precio; }

    public double getAlquiler(){ return alquiler; }

    public Jugador getPropietario(){ return propietario;}

    public int getUbicacion() { return this.ubicacion; }

    public void setUbicacion(int ubicacion){ this.ubicacion = ubicacion; }

    public abstract String setPropietario(Jugador propietario);

    public void liberar() {
        this.propietario = null;
        this.estado = EstadoPropiedades.EN_VENTA;
    }

    public abstract void venderComprable();

}
