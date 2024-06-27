package org.example.model;
import org.fusesource.jansi.Ansi;

public class Acciones {
    public Accion getAccion(int accion,EstadoAcciones estadoAcciones){
        if (estadoAcciones == EstadoAcciones.PRESO){
            return getAccionPreso(accion);
        }
        else if(estadoAcciones == EstadoAcciones.SIN_PROPIEADES){
            return getAccionSinPropiedades(accion);
        }
        else if(estadoAcciones == EstadoAcciones.CON_CASA){
            return getAccionConPropiedades(accion);
        }
        else if(estadoAcciones == EstadoAcciones.CON_BARRIO){
            return getAccionConBarrio(accion);
        }

        return null;
    }

    public String acciones(Ansi colorANSI,Ansi resetColor){
        String mensaje = "";
        for (int i = 0; i < Accion.values().length-2; i++){
            mensaje += ( colorANSI+""+ i +" -> " +  Accion.values()[i] + resetColor +"\n");
        }
        return mensaje;
    }

    public String accionesJugadorPreso(Ansi colorANSI,Ansi resetColor){
        int indice = 0;
        String mensaje = "";
        for (int i = 0; i < Accion.values().length; i++){
            if (Accion.values()[i].equals(Accion.PAGAR_FIANZA) || Accion.values()[i].equals(Accion.TIRAR_DADOS) ){
                mensaje += (colorANSI+ ""+  indice+ " -> " +  Accion.values()[i] + resetColor + "\n");
                indice ++;
            }
        }
        return mensaje;
    }

    public String accionesJugadorSinPropiedad(Ansi colorANSI,Ansi resetColor){
        int indice = 0;
        String mensaje = "";
        for (int i = 0; i < Accion.values().length; i++){
            if (Accion.values()[i].equals(Accion.COMPRAR) || Accion.values()[i].equals(Accion.TERMINAR_TURNO) || Accion.values()[i].equals(Accion.CONSULTAR_PRECIO_CASA)  ){
                mensaje += (colorANSI+ ""+  indice+ " -> " +  Accion.values()[i] + resetColor + "\n");
                indice ++;
            }
        }
        return mensaje;
    }

    public String accionesJugadorConPropiedad(Ansi colorANSI,Ansi resetColor){
        int indice = 0;
        String mensaje = "";
        for (int i = 0; i < Accion.values().length; i++){
            if (Accion.values()[i].equals(Accion.COMPRAR) || Accion.values()[i].equals(Accion.TERMINAR_TURNO) || Accion.values()[i].equals(Accion.CONSULTAR_PRECIO_CASA) || Accion.values()[i].equals(Accion.VENDER) || Accion.values()[i].equals(Accion.HIPOTECAR) || Accion.values()[i].equals(Accion.DESHIPOTECAR)  ){
                mensaje += (colorANSI+ ""+  indice+ " -> " +  Accion.values()[i] + resetColor + "\n");
                indice ++;
            }
        }
        return mensaje;
    }

    public String accionesJugadorConBarrio(Ansi colorANSI,Ansi resetColor){
        int indice = 0;
        String mensaje = "";
        for (int i = 0; i < Accion.values().length; i++){
            if (Accion.values()[i].equals(Accion.COMPRAR) || Accion.values()[i].equals(Accion.TERMINAR_TURNO) || Accion.values()[i].equals(Accion.CONSTRUIR) ||Accion.values()[i].equals(Accion.CONSULTAR_PRECIO_CASA) || Accion.values()[i].equals(Accion.VENDER) || Accion.values()[i].equals(Accion.HIPOTECAR) || Accion.values()[i].equals(Accion.DESHIPOTECAR)  ){
                mensaje += (colorANSI+ ""+  indice+ " -> " +  Accion.values()[i] + resetColor + "\n");
                indice ++;
            }
        }
        return mensaje;
    }

    private Accion getAccionPreso(int accion) {
        if (accion == 1){
            return Accion.PAGAR_FIANZA;
        }
        return Accion.TIRAR_DADOS;
    }

    private Accion getAccionSinPropiedades(int accion) {
        if (accion == 0){
            return Accion.TERMINAR_TURNO;
        }else if(accion == 1){
            return Accion.CONSULTAR_PRECIO_CASA;
        }
        else if(accion == 2){
            return Accion.COMPRAR;
        }
        else {
            return null;
        }
    }

    private Accion getAccionConPropiedades(int accion) {
        if (accion == 0){
            return Accion.TERMINAR_TURNO;
        }else if(accion == 1){
            return Accion.CONSULTAR_PRECIO_CASA;
        }
        else if(accion == 2){
            return Accion.VENDER;
        }
        else if(accion == 3){
            return Accion.COMPRAR;
        }
        else if(accion == 4){
            return Accion.HIPOTECAR;
        }
        else if(accion == 5){
            return Accion.DESHIPOTECAR;
        }
        else{
            return null;
        }
    }

    private Accion getAccionConBarrio(int accion) {
        if (accion == 0){
            return Accion.TERMINAR_TURNO;
        }else if(accion == 1){
            return Accion.CONSTRUIR;
        }
        else if(accion == 2){
            return Accion.CONSULTAR_PRECIO_CASA;
        }
        else if(accion == 3){
            return Accion.VENDER;
        }
        else if(accion == 4){
            return Accion.COMPRAR;
        }
        else if(accion == 5){
            return Accion.HIPOTECAR;
        }
        else if(accion == 6){
            return Accion.DESHIPOTECAR;

        }else{
            return null;
        }
    }
}