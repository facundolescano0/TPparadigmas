package org.example.controller;
import org.example.model.Barrio;
import org.example.model.Construcciones;
import org.example.model.Jugador;
import org.example.model.Propiedad;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ConstruccionController {
    private ArrayList<Barrio> barrios;

    public ConstruccionController(ArrayList<Barrio> barrios) {
        this.barrios = barrios;
    }

    public String venderSiendoPropietarioBarrio(Jugador jugador,Propiedad propiedad, Barrio barrio) {
        if(propiedad.getConstrucciones() == Construcciones.SIN_CASA){
            if (validarVentaTerreno(barrio)){
                String mensaje = "";
                double precioReventa = propiedad.getPrecio()*Constantes.PORCENTAJE_DE_VENTA;
                jugador.restarPatrimonio(precioReventa);
                mensaje += propiedad.venderComprable();
                propiedad.liberar();
                mensaje += ("Propiedad vendida con exito! \n Ahora tienes $" + jugador.getPlata());
                return mensaje;
            }
        }else{
            if (validarVenta(jugador,propiedad)){
                double precioReventa = propiedad.getPrecioCasa();
                jugador.sumarPlata(precioReventa);
                return deconstruirCasa(barrio,jugador,propiedad);
            }else{
                return ("ERROR: EL JUGADOR " + jugador.getNombre() + " NO PUEDE VENDER DEBIDO A QUE EXCEDE EL LIMITE DE DIFERENCIA ENTRE LAS CONTRUCCIONES DE LAS PROPIEDADES");
            }
        }
        return "No se puede vender esa propiedad";
    }
    public String vender(Jugador jugador,Propiedad propiedad){
        Barrio barrio = barrios.get(propiedad.getBarrio());
        if (barrio.getPropietario() == jugador){
            return venderSiendoPropietarioBarrio(jugador,propiedad,barrio);
        }else{
            double precioReventa = propiedad.getPrecio()*Constantes.PORCENTAJE_DE_VENTA;
            jugador.restarPatrimonio(precioReventa);
            String mensaje = "";
            mensaje +=  propiedad.venderComprable();
            propiedad.liberar();
            mensaje += ("Propiedad vendida con exito!");
            return mensaje;
        }
    }

    public boolean validarVentaTerreno(Barrio barrio){
        List<Propiedad> propiedadList = barrio.getPropiedades();
        for (Propiedad prop: propiedadList) {
            if (prop.getConstrucciones() != Construcciones.SIN_CASA) {
                return false;
            }
        }
        return true;
    }

    public boolean validarVenta(Jugador jugador,Propiedad propiedad){
        ArrayList<Propiedad> listaDePropiedades = this.barrios.get(propiedad.getBarrio()).getPropiedades();
        for (Propiedad propiedadLista : listaDePropiedades) {
            int dif = Math.abs(propiedad.getConstrucciones().ordinal()- 1 - propiedadLista.getConstrucciones().ordinal()) ;
            if (dif > 1) {
                return false;
            }
        }
        return true;
    }

    public boolean validarConstruccion(Jugador jugador,int barrio,Propiedad propiedad){
        if (!esPropietarioBarrio(jugador,barrios.get(barrio))||jugador.getPlata() < propiedad.getPrecioCasa() || propiedad.getConstrucciones() == Construcciones.HOTEL){
            return false;
        }
        ArrayList<Propiedad> listaDePropiedades = barrios.get(propiedad.getBarrio()).getPropiedades();
        for (Propiedad listaDePropiedade : listaDePropiedades) {
            int dif = propiedad.getConstrucciones().ordinal() - listaDePropiedade.getConstrucciones().ordinal();
            if (dif > 0) {
                return false;
            }
        }
        return true;
    }

    public String construirEnPropiedad(Jugador jugador,Propiedad propiedad){
        if (validarConstruccion(jugador, propiedad.getBarrio(),propiedad)){
            String mensaje = "";
            propiedad.sumarConstruccion();
            propiedad.actualizarAlquiler();
            jugador.restarPlata(propiedad.getPrecioCasa());
            jugador.sumarAlPatrimonio(propiedad.getPrecioCasa()* Constantes.PORCENTAJE_DE_VENTA);
            mensaje += ("Propiedad mejorada a "+propiedad.getConstrucciones()+" con exito \n");
            mensaje += ("Ahora tienes $" + jugador.getPlata());
            return mensaje;
        }else{
            return "No se puede construir en esa propiedad";
        }
    }

    public boolean esPropietarioBarrio(Jugador jugador,Barrio barrio){
        ArrayList<Propiedad> listaDePropiedades = barrio.getPropiedades();
        for (Propiedad propiedad : listaDePropiedades){
            if (propiedad.getPropietario() != jugador){
                return false;
            }
        }
        barrio.setPropietarioBarrio(jugador);
        return true;
    }

    public String deconstruirCasa(Barrio barrio, Jugador jugador,Propiedad propiedad){
            jugador.restarPatrimonio(propiedad.getPrecioCasa());
            propiedad.restarConstruccion();
            propiedad.actualizarAlquiler();
            return ("Propiedad fue reducida a "+propiedad.getConstrucciones());
    }
}
