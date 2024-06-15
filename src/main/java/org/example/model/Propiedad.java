package org.example.model;


import org.example.controller.CheckHipotecar;
import org.example.controller.Constantes;
import java.util.Arrays;
import java.util.List;

public class Propiedad extends Comprable {
    private int numeroDeBarrio;
    private Construcciones construcciones;
    private double precioCasa;

    public Propiedad(int precio, int numeroDeBarrio, int ubicacion) {
        super(precio, ubicacion,true);
        this.numeroDeBarrio = numeroDeBarrio;
        this.construcciones = Construcciones.SIN_CASA;
        this.precioCasa = (precio * Constantes.PORCENTAJE_PRECIO_CASA);
    }

    public double getPrecioCasa() {
        return precioCasa;
    }

    public String getNombrePropietario() {
        return propietario.getNombre();
    }

    public int getBarrio() {
        return numeroDeBarrio;
    }

    public String setPropietario(Jugador propietario) {
        if (this.propietario == null) {
            this.propietario = propietario;
            this.estado = EstadoPropiedades.COMPRADO;
            propietario.agregarPropiedad(this);
            System.out.println("Propiedad comprada con exito");
            propietario.restarPlata(precio);
            System.out.println("Le quedan $" + propietario.getPlata());
            return null;
        }
        if (propietario!= null){
            System.out.println("La propiedad ya fue comprada");
        }
        return null;
    }

    public Construcciones getConstrucciones() {
        return this.construcciones;
    }

    public void sumarConstruccion() {
        construcciones = siguienteConstruccion();
    }

    public void restarConstruccion() {
        construcciones = anteriorConstruccion();
    }

    private Construcciones anteriorConstruccion() {
        List<Construcciones> construccionesLista = Arrays.asList(Construcciones.values());
        return construccionesLista.get(construccionesLista.indexOf(construcciones) - 1);
    }

    private Construcciones siguienteConstruccion() {
        List<Construcciones> construccionesLista = Arrays.asList(Construcciones.values());
        return construccionesLista.get(construccionesLista.indexOf(construcciones) + 1);
    }

    public void hipotecar(Barrio barrio, Jugador jugador) {
        CheckHipotecar controladorHipoteca = new CheckHipotecar(jugador, barrio, this);
        if (controladorHipoteca.validarHipotecar()) {
            System.out.println("PROPIEDAD " + this.ubicacion + " FUE HIPOTECADA CON ÉXITO.\nSe le reintegró el 60% de la propiedad");
            ponerEnHipoteca();
            jugador.sumarPlata(this.getPrecio()*Constantes.PORCENTAJE_HIPOTECA);
            System.out.println("Ahora tienes $"+ jugador.getPlata());
            jugador.restarPatrimonio(this.getPrecio());
        }
    }

    public void deshipotecar(Jugador jugador) {
        if (jugador == this.propietario && this.estado == EstadoPropiedades.HIPOTECADO && jugador.getPlata()>= this.getPrecio()* Constantes.PORCENTAJE_DE_DESHIPOTECAR){
            System.out.println("SU PROPIEDAD SE DESHIPOTECO CON EXITO");
            estado = EstadoPropiedades.COMPRADO;
            jugador.restarPlata((this.getPrecio()* Constantes.PORCENTAJE_DE_DESHIPOTECAR));
            jugador.sumarAlPatrimonio(this.getPrecio()* Constantes.PORCENTAJE_DE_VENTA); // Se suma el maximo posible (de venta).
            System.out.println("Ahora tienes $"+ jugador.getPlata());
        }else if (jugador.getPlata()< this.getPrecio()* Constantes.PORCENTAJE_DE_DESHIPOTECAR){
            System.out.println("ERROR: NO ES POSIBLE HIPOTECAR SU PROPIEDAD - SIN SALDO SUFICIENTE");
        }
        else{
            System.out.println("ERROR: NO ES POSIBLE HIPOTECAR SU PROPIEDAD");
        }
    }

    private void ponerEnHipoteca() {
        estado = EstadoPropiedades.HIPOTECADO;
    }

    public void liberar() {
        this.construcciones = Construcciones.SIN_CASA;
        this.propietario = null;
        this.estado = EstadoPropiedades.EN_VENTA;
        this.alquiler = setPrecioBaseAlquiler();
    }

    public void
    venderComprable() {
        this.propietario.sumarPlata(precio * Constantes.PORCENTAJE_DE_VENTA);
        this.propietario.eliminarComprable(this);
        System.out.println("Ahora tiene $" + propietario.getPlata());
    }

    public void actualizarAlquiler() {
        this.alquiler = (alquiler * Constantes.PORCENTAJE_ALQUILER_NUEVA_CASA) + alquiler;
    }

    private int setPrecioBaseAlquiler() {
        return (int) (precio * Constantes.PORCENTAJE_ALQUILER);
    }

}




