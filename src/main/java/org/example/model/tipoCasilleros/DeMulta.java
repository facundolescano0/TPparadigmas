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

    public void ejecutarCasillero(Jugador jugador) {
        if(jugador.getPatrimonioTotal() < montoMulta) {
            System.out.println();
            System.out.println("EL JUGADOR " + jugador.getNombre() + "ENTRÓ EN BANCARROTA. SIN DINERO SUFICIENTE.");
            jugador.setQuiebra();
        }else{
            if (jugador.restarPlata(montoMulta)){
                System.out.println(jugador.getNombre() + " pagaste multa de $"+montoMulta);
            }else{
                System.out.println(jugador.getNombre() +" ¡no tienes dinero suficiente para pagar la multa!\nDEBES HIPOTECAR O VENDER SI O SI ANTES DE AVANZAR, SINO VA A PERDER");
                System.out.println("Antes de avanzar el turno debes tener $" + montoMulta + " sino perderás automaticamente. La deuda se paga al final de su turno.");
                jugador.setDeuda();
            }
        }
    }
}
