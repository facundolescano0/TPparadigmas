package org.example.model;

public class EstacionTransporte extends Comprable{

    public EstacionTransporte(double precio, int ubicacion) {
        super(precio, ubicacion,false);
        this.propietario = null;
    }

    @Override
    public String setPropietario(Jugador propietario) {
        if(this.propietario == null){
            this.propietario = propietario;
            this.estado = EstadoPropiedades.COMPRADO;
            //System.out.println("Estacion comprada con exito");
            propietario.restarPlata(precio);
            System.out.println("Le quedan $" + propietario.getPlata());
            propietario.agregarEstacion(this);
            return ("Estacion comprada con exito");
        }else{
            //System.out.println("La estacion ya fue comprada");
            return ("La estacion ya fue comprada");
        }
    }

    public void venderComprable(){
        propietario.sumarPlata(precio);
        propietario.eliminarComprable(this);
        setPropietario(null);
        this.estado = EstadoPropiedades.EN_VENTA;
    }

    public String getNombrePropietario() {
        return propietario.getNombre();
    }
}
