package py.edu.uc.lp3.domain;

import py.edu.uc.lp3.exceptions.ArmaInvalidaException;

public abstract class ArmaDeFuego extends Arma {
    private int dano;
    private int precision;
    private double recarga;
    private double velocidad;

    public ArmaDeFuego(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad) {
        super(nombre, id, precio);
        setDano(dano);
        setPrecision(precision);
        setRecarga(recarga);
        setVelocidad(velocidad);
    }

    public int getDano() { return dano; }

    public void setDano(int dano) {
        if (dano < 0) {
            throw new ArmaInvalidaException("El daño no puede ser negativo: " + dano);
        }
        this.dano = dano;
    }

    public int getPrecision() { return precision; }

    public void setPrecision(int precision) {
        if (precision < 0 || precision > 100) {
            throw new ArmaInvalidaException("La precisión debe estar entre 0 y 100: " + precision);
        }
        this.precision = precision;
    }

    public double getRecarga() { return recarga; }

    public void setRecarga(double recarga) {
        if (recarga <= 0) {
            throw new ArmaInvalidaException("La recarga debe ser mayor a 0: " + recarga);
        }
        this.recarga = recarga;
    }

    public double getVelocidad() { return velocidad; }

    public void setVelocidad(double velocidad) {
        if (velocidad < 0) {
            throw new ArmaInvalidaException("La velocidad de disparo no puede ser negativa: " + velocidad);
        }
        this.velocidad = velocidad;
    }

    @Override
    public String describir() {
        return "nombre=" + getNombre()
                + ", id=" + getId()
                + ", precio=" + getPrecio()
                + ", dano=" + dano
                + ", precision=" + precision
                + ", recarga=" + recarga
                + ", velocidad=" + velocidad;
    }
}
