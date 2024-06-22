package org.example.model;
import org.fusesource.jansi.Ansi;
import org.example.model.Accion;

public class Acciones {
    public Accion getAccion(int accion){
        if(accion >= 0 && accion < Accion.values().length){
            return Accion.values()[accion];
        }
        return null;
    }


    public String accionesDisponibles(Ansi colorANSI,Ansi resetColor,EstadoAcciones acciones) {
        if (acciones == EstadoAcciones.CON_BARRIO) {
            return accionesJugadorConBarrio(colorANSI, resetColor);
        } else if (acciones == EstadoAcciones.CON_CASA) {
            return accionesJugadorConPropiedad(colorANSI, resetColor);
        } else if (acciones == EstadoAcciones.SIN_PROPIEADES) {
            return accionesJugadorSinPropiedad(colorANSI, resetColor);
        } else {
            return null;
        }
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
            if (Accion.values()[i].equals(Accion.COMPRAR) || Accion.values()[i].equals(Accion.TERMINAR_TURNO) || Accion.values()[i].equals(Accion.CONSULTAR_PRECIO_CASA) || Accion.values()[i].equals(Accion.TIRAR_DADOS) ){
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
            if (Accion.values()[i].equals(Accion.COMPRAR) || Accion.values()[i].equals(Accion.TERMINAR_TURNO) || Accion.values()[i].equals(Accion.CONSULTAR_PRECIO_CASA) || Accion.values()[i].equals(Accion.VENDER) || Accion.values()[i].equals(Accion.HIPOTECAR) || Accion.values()[i].equals(Accion.DESHIPOTECAR) || Accion.values()[i].equals(Accion.TIRAR_DADOS) ){
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
            if (Accion.values()[i].equals(Accion.COMPRAR) || Accion.values()[i].equals(Accion.TERMINAR_TURNO) || Accion.values()[i].equals(Accion.CONSTRUIR) ||Accion.values()[i].equals(Accion.CONSULTAR_PRECIO_CASA) || Accion.values()[i].equals(Accion.VENDER) || Accion.values()[i].equals(Accion.HIPOTECAR) || Accion.values()[i].equals(Accion.DESHIPOTECAR) || Accion.values()[i].equals(Accion.TIRAR_DADOS) ){
                mensaje += (colorANSI+ ""+  indice+ " -> " +  Accion.values()[i] + resetColor + "\n");
                indice ++;
            }
        }
        return mensaje;
    }

    public Accion getAccionPreso(int accion) {
        if (accion == 1){
            return this.getAccion(8);
        }
        return this.getAccion(0);
    }
}