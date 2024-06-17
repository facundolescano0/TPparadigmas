package org.example.model.tipoCasilleros;

import org.example.model.*;

public class Estacion extends Casillero implements CasilleroEjecutable{
    private double precio;
    private final EstacionTransporte estacion;

    public Estacion(int ubicacion,double precio){
        super("De transporte",TipoCasillero.ESTACION,ubicacion,true,true);
        this.precio = precio;
        this.estacion = new EstacionTransporte(precio,ubicacion);
        this.EsEjecutable = true;
    }

    @Override
    public EstacionTransporte getPropiedad() {
        return estacion;
    }

    @Override
    public String ejecutarCasillero(Jugador jugador) {
        Jugador propietario = estacion.getPropietario();
        if (estacion.getEstado() == EstadoPropiedades.COMPRADO && propietario != jugador) {
            if(jugador.getPatrimonioTotal() < estacion.getAlquiler()) {
                jugador.setQuiebra();
                return ("\n EL JUGADOR " + jugador.getNombre() + "ENTRÓ EN BANCARROTA. SIN DINERO SUFICIENTE.");

            }else{
                double alquiler = estacion.getAlquiler();
                propietario.sumarPlata(alquiler);
                if (jugador.restarPlata(alquiler)){
                    return String.format("%s pagaste %f de alquiler por estar en la estacion de %s\n",jugador.getNombre(),alquiler,estacion.getNombrePropietario());
                }else{
                    jugador.setDeuda();
                    return (jugador.getNombre() +"¡no tienes dinero suficiente para pagar el alquiler de esta estacion!\n\tDEBES HIPOTECAR SI O SI ANTES DE AVANZAR, SINO VA A PERDER\n Antes de avanzar el turno debes tener $" + estacion.getPrecio() + " sino perderás automaticamente. La deuda se paga al final de su turno.");

                }
            }
        }
        return "";
    }

    public EstacionTransporte getEstacion(){ return this.estacion; }

    public double getPrecio(){ return this.precio; }
}
