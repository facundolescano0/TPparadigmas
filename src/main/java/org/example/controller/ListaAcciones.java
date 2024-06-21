package org.example.controller;

import org.example.model.Jugador;
import org.example.model.tipoCasilleros.Casillero;

public interface ListaAcciones {
    String construir(Jugador jugador, int propiedad, ConstruccionController controller);
    String consultar_precio_casa(Jugador jugador, int propiedad, ConstruccionController controller);
    String vender(Jugador jugador, int propiedad, ConstruccionController controller);
    String hipotecar(Jugador jugador, int propiedad, ConstruccionController controller);
    String comprar(Jugador jugador, int propiedad, ConstruccionController controller);
    String deshipotecar(Jugador jugador, int propiedad,  ConstruccionController controller);
    String pagar_fianza(Jugador jugador,Casillero carcel);
}


