package org.example.model.tipoCasilleros;
import org.example.model.Comprable;
import org.example.model.Propiedad;

abstract public class Casillero {
    protected String efecto;
    protected TipoCasillero tipo;
    protected int ubicacion;
    protected Boolean EsEjecutable;
    protected Boolean EsComprable ;
    protected Boolean EsPropiedad = false;

    public Casillero(String efecto, TipoCasillero tipoCasillero, int ubicacion,boolean condicion,boolean condicion2) {
        this.efecto = efecto;
        this.tipo = tipoCasillero;
        this.ubicacion = ubicacion;
        this.EsEjecutable = condicion;
        this.EsComprable = condicion2;
    }
    public Boolean getEsCasilleroPropiedad(){
        return EsPropiedad;
    }

    public Boolean getEsComprable(){
        return EsComprable;
    }

    public Boolean getEsEjecutable(){
        return EsEjecutable;
    }

    public TipoCasillero getTipo() {return tipo;}

    public String getEfecto(){return efecto;}

    public int getUbicacion(){return ubicacion;}

    public double getPrecio(){
        return 0;
    }

    public  <T extends Comprable> T getPropiedad(){
        return null;
    }

    public double getFianza(){return 0;}
}





