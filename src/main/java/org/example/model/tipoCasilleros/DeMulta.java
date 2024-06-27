package org.example.model.tipoCasilleros;

import org.example.model.Jugador;

public class DeMulta extends Casillero implements CasilleroEjecutable {
    private double montoMulta;
    public DeMulta(int ubicacion,double montoMulta){
        super("Multa",TipoCasillero.MULTA,ubicacion,true,false);
        this.montoMulta = montoMulta;
    }

    @Override
    public double getPrecio(){
        return montoMulta;
    }

    public String ejecutarCasillero(Jugador jugador) {
        if(jugador.getPatrimonioTotal() < montoMulta) {
            jugador.setQuiebra();
            return ("EL JUGADOR " + jugador.getNombre() + "ENTRÓ EN BANCARROTA. SIN DINERO SUFICIENTE.");
        }else{
            if (jugador.restarPlata(montoMulta)){
                return jugador.getNombre() + " pagaste multa de $"+montoMulta;
            }else{
                jugador.setDeuda();
                return jugador.getNombre() +" ¡no tienes dinero suficiente para pagar la multa!\nDEBES HIPOTECAR O VENDER SI O SI ANTES DE AVANZAR, SINO VA A PERDER";
            }
        }
    }
}