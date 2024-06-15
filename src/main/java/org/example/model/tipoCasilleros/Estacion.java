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
    public void ejecutarCasillero(Jugador jugador) {
        Jugador propietario = estacion.getPropietario();
        if (estacion.getEstado() == EstadoPropiedades.COMPRADO && propietario != jugador) {
            if(jugador.getPatrimonioTotal() < estacion.getAlquiler()) {
                System.out.println();
                System.out.println("EL JUGADOR " + jugador.getNombre() + "ENTRÓ EN BANCARROTA. SIN DINERO SUFICIENTE.");
                jugador.setQuiebra();
            }else{
                double alquiler = estacion.getAlquiler();
                propietario.sumarPlata(alquiler);
                if (jugador.restarPlata(alquiler)){
                    System.out.printf("%s pagaste %f de alquiler por estar en la estacion de %s\n",jugador.getNombre(),alquiler,estacion.getNombrePropietario());
                }else{
                    System.out.println(jugador.getNombre() +"¡no tienes dinero suficiente para pagar el alquiler de esta estacion!\n\tDEBES HIPOTECAR SI O SI ANTES DE AVANZAR, SINO VA A PERDER");
                    System.out.println("Antes de avanzar el turno debes tener $" + estacion.getPrecio() + " sino perderás automaticamente. La deuda se paga al final de su turno.");
                    jugador.setDeuda();
                }
            }
        }
    }

    public EstacionTransporte getEstacion(){ return this.estacion; }

    public double getPrecio(){ return this.precio; }
}
