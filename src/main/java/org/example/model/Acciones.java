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

    public String acciones(Ansi colorANSI,Ansi resetColor){
        String mensje = "";
        for (int i = 0; i < Accion.values().length-2; i++){
            mensje += ( colorANSI+""+ i +" -> " +  Accion.values()[i] + resetColor +"\n");
        }
        return mensje;
    }

    public String accionesJugadorPreso(Ansi colorANSI,Ansi resetColor){
        int indice = 0;
        String mensje = "";
        for (int i = 0; i < Accion.values().length; i++){
            if (Accion.values()[i].equals(Accion.PAGAR_FIANZA) || Accion.values()[i].equals(Accion.TIRAR_DADOS) ){
                mensje += (colorANSI+ ""+  indice+ " -> " +  Accion.values()[i] + resetColor + "\n");
                indice ++;
            }
        }
        return mensje;
    }

    public Accion getAccionPreso(int accion) {
        if (accion == 1){
            return this.getAccion(8);
        }
        return this.getAccion(0);
    }
}