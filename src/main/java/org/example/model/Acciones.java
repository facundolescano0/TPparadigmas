package org.example.model;

public class Acciones {

    public enum Accion {TERMINAR_TURNO,CONSTRUIR,REFORMAR,VENDER,HIPOTECAR,
        COMPRAR,PAGAR_FIANZA, DESHIPOTECAR;

        private static final Accion[] acciones = values();

        public static Accion getAccion(int accion){
            if(accion < acciones.length){ return acciones[accion]; }
            return null;
        }
    }

    public static void mostrarAcciones(){
        for (int i = 0; i < Accion.acciones.length; i++){
            System.out.println(i +" -> " +  Accion.getAccion(i));
        }
    }
}
