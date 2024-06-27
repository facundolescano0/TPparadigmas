package org.example.model.tipoCasilleros;

import org.example.model.*;
import org.example.model.EstadoPropiedades;

public class DePropiedad extends Casillero implements CasilleroEjecutable {
    private final Propiedad propiedad;

    public DePropiedad(int casillero, int precio, int numeroDeBarrio) {
        super("Propiedad", TipoCasillero.PROPIEDAD, casillero,true,true);
        this.propiedad = new Propiedad(precio, numeroDeBarrio, casillero);
        this.EsPropiedad = true;
    }

    @Override
    public Propiedad getPropiedad() {
        return propiedad;
    }

    public double getPrecio() {
        return propiedad.getPrecio();
    }

    @Override
    public String ejecutarCasillero(Jugador jugador) {
        Jugador propietario = propiedad.getPropietario();
        if (propiedad.getEstado() == EstadoPropiedades.COMPRADO && propietario != jugador) {
            if(jugador.getPatrimonioTotal() < propiedad.getAlquiler()) {
                jugador.setQuiebra();
<<<<<<< HEAD
                return("\n EL JUGADOR " + jugador.getNombre() + "ENTRÓ EN BANCARROTA. SIN DINERO SUFICIENTE.");
=======
                return ("EL JUGADOR " + jugador.getNombre() + "ENTRÓ EN BANCARROTA. SIN DINERO SUFICIENTE.");
>>>>>>> CorreccionTP1
            }else{
                double alquiler = propiedad.getAlquiler();
                if (jugador.restarPlata(alquiler)){
                    propietario.sumarPlata(alquiler);
<<<<<<< HEAD
                    return( jugador.getNombre() + " pagaste " + alquiler + "de alquiler por estar en la propiedad de "+ propiedad.getNombrePropietario() + "\n");
                }else{
                    jugador.setDeuda();
                    return (jugador.getNombre() +"¡no tienes dinero suficiente para pagar el alquiler de esta propiedad!\n\tDEBES HIPOTECAR SI O SI ANTES DE AVANZAR, SINO VA A PERDER\n Antes de avanzar el turno debes tener $" + propiedad.getPrecio() + " sino perderás automaticamente. La deuda se paga al final de su turno.");

                }
            }
        }
    return "";
=======
                    return String.format("%s pagaste %f de alquiler por estar en la propiedad de %s\n",jugador.getNombre(),alquiler,propiedad.getNombrePropietario());
                }else{
                    jugador.setDeuda();
                    return jugador.getNombre() +"¡no tienes dinero suficiente para pagar el alquiler de esta propiedad!\n\tDEBES HIPOTECAR SI O SI ANTES DE AVANZAR, SINO VA A PERDER";
                }
            }
        }
        return "";
>>>>>>> CorreccionTP1
    }
}