package org.example.controller;

import org.example.model.Jugador;
import org.example.model.tipoCasilleros.Casillero;

public class FachadaAcciones implements ListaAcciones{
    private Hipotecar hipoteca;
    private Comprar comprar;
    private Vender vender;
    private ConsultarPrecios consultarPrecios;
    private Construir construir;
    private Deshipotecar deshipotecar;
    private PagarFianza pagarFianza;

    public FachadaAcciones(Hipotecar hipoteca,Comprar comprar,Vender vender,ConsultarPrecios consultarPrecios,Construir construir,Deshipotecar deshipotecar,PagarFianza pagarFianza) {
        this.hipoteca = hipoteca;
        this.comprar = comprar;
        this.vender = vender;
        this.consultarPrecios = consultarPrecios;
        this.construir = construir;
        this.deshipotecar = deshipotecar;
        this.pagarFianza = pagarFianza;
    }

    @Override
    public String comprar(Jugador jugador,int num,ConstruccionController controller) {
        return comprar.ejecutar(jugador,num,controller);
    }

    @Override
    public String construir(Jugador jugador, int propiedad, ConstruccionController controller) {
        return construir.ejecutar(jugador,propiedad,controller);
    }

    @Override
    public String deshipotecar(Jugador jugador, int propiedad, ConstruccionController controller) {
        return deshipotecar.ejecutar(jugador,propiedad,controller);
    }

    @Override
    public String hipotecar(Jugador jugador, int propiedad, ConstruccionController controller) {
        return hipoteca.ejecutar(jugador,propiedad,controller);
    }

    @Override
    public String consultar_precio_casa(Jugador jugador, int propiedad,  ConstruccionController controller) {
        return consultarPrecios.ejecutar(jugador,propiedad,controller);
    }

    @Override
    public String vender(Jugador jugador, int propiedad,  ConstruccionController controller) {
        return vender.ejecutar(jugador,propiedad,controller);
    }

    @Override
    public String pagar_fianza(Jugador jugador,Casillero carcel){
        return pagarFianza.ejecutar(jugador,carcel);
    }
}
