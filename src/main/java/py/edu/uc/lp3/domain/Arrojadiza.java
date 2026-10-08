package py.edu.uc.lp3.domain;

import py.edu.uc.lp3.exceptions.ArmaInvalidaException;

public abstract class Arrojadiza extends Arma {
    private String tipo;
    private double radio;
    private double distancia;
    private double duracion;

    public Arrojadiza(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion) {
        super(nombre, id, precio);
        setTipo(tipo);
        setRadio(radio);
        setDistancia(distancia);
        setDuracion(duracion);
    }

    public String getTipo() { return tipo; }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new ArmaInvalidaException("El tipo de arma arrojadiza no puede estar vacío");
        }
        this.tipo = tipo;
    }

    public double getRadio() { return radio; }

    public void setRadio(double radio) {
        if (radio <= 0) {
            throw new ArmaInvalidaException("El radio de cobertura debe ser mayor a 0: " + radio);
        }
        this.radio = radio;
    }

    public double getDistancia() { return distancia; }

    public void setDistancia(double distancia) {
        if (distancia <= 0) {
            throw new ArmaInvalidaException("La distancia de lanzamiento debe ser mayor a 0: " + distancia);
        }
        this.distancia = distancia;
    }

    public double getDuracion() { return duracion; }

    public void setDuracion(double duracion) {
        if (duracion <= 0) {
            throw new ArmaInvalidaException("La duración debe ser mayor a 0: " + duracion);
        }
        this.duracion = duracion;
    }

    @Override
    public String describir() {
        return "nombre=" + getNombre()
                + ", id=" + getId()
                + ", precio=" + getPrecio()
                + ", tipo=" + tipo
                + ", radio=" + radio
                + ", distancia=" + distancia
                + ", duracion=" + duracion;
    }
}
