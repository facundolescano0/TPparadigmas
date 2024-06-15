package org.example.model.tipoCasilleros;
import org.example.model.Estado;
import org.example.model.Jugador;

import java.io.Serializable;

public class IrALaCarcel extends Casillero implements CasilleroEjecutable{
    private final int duracionPena;
    private final int ubicacionCarcel;

    public IrALaCarcel(int ubicacion, int duracionPena,int ubicacionCarcel) {
        super("IrALaCarcel",TipoCasillero.IR_A_LA_CARCEL,ubicacion,true,false);
        this.duracionPena = duracionPena;
        this.ubicacionCarcel = ubicacionCarcel;
        this.EsEjecutable = true;
    }

    public void ejecutarCasillero(Jugador jugador) {
        jugador.setEstado(Estado.Preso);
        jugador.setCondena(duracionPena);
        jugador.setUbicacion(ubicacionCarcel);
        System.out.printf("Ups! %s se va preso %d turnos\n", jugador.getNombre(), duracionPena);
    }
}
