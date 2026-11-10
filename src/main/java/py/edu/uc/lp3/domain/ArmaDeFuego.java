package py.edu.uc.lp3.domain;

import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.CargaMunicionException;
import py.edu.uc.lp3.exceptions.DanoInvalidoException;

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

    public int reglaDeDano(int danoAplicado, int municionMaxima) {
        if (danoAplicado < 0) {
            throw new DanoInvalidoException("El daño aplicado no puede ser negativo: " + danoAplicado);
        }
        if (municionMaxima <= 0) {
            throw new DanoInvalidoException("La munición máxima debe ser mayor a 0 para evaluar el daño");
        }
        int limite = municionMaxima * 5;
        if (danoAplicado > limite) {
            throw new DanoInvalidoException("El daño excede el límite permitido para esta arma: " + danoAplicado);
        }
        return danoAplicado;
    }

    public int reglaDeCargaMunicion(int municionActual, int cantidad, int municionMaxima) {
        if (cantidad <= 0) {
            throw new CargaMunicionException("La cantidad a cargar debe ser mayor a 0");
        }
        if (municionMaxima <= 0) {
            throw new CargaMunicionException("La munición máxima debe ser mayor a 0: " + municionMaxima);
        }
        if (municionActual < 0) {
            throw new CargaMunicionException("La munición actual no puede ser negativa: " + municionActual);
        }
        int nuevaMunicion = municionActual + cantidad;
        if (nuevaMunicion > municionMaxima) {
            throw new CargaMunicionException(
                    "Carga inválida: munición actual (" + municionActual
                            + ") + cantidad (" + cantidad
                            + ") supera la capacidad máxima (" + municionMaxima + ")");
        }
        return nuevaMunicion;
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
