package py.edu.uc.lp3.domain;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Arrojadiza extends Arma {
    private String tipo;
    private double radio;
    private double distancia;
    private double duracion;

    protected Arrojadiza() {
        super();
    }

    public Arrojadiza(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion) {
        super(nombre, id, precio);
        setTipo(tipo);
        setRadio(radio);
        setDistancia(distancia);
        setDuracion(duracion);
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getRadio() { return radio; }
    public void setRadio(double radio) { this.radio = radio; }

    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) { this.distancia = distancia; }

    public double getDuracion() { return duracion; }
    public void setDuracion(double duracion) { this.duracion = duracion; }

    @Override
    public String describir() {
        return "nombre=" + getNombre()
                + ", id=" + getId()
                + ", precio=" + getPrecio()
                + ", durabilidad=" + getDurabilidad()
                + ", tipo=" + tipo
                + ", radio=" + radio
                + ", distancia=" + distancia
                + ", duracion=" + duracion;
    }
}
