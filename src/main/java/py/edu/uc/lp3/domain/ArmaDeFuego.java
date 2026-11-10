package py.edu.uc.lp3.domain;

import jakarta.persistence.MappedSuperclass;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.CargaMunicionException;

@MappedSuperclass
public abstract class ArmaDeFuego extends Arma {
    private int dano;
    private int precision;
    private double recarga;
    private double velocidad;
    private int municionActual;
    private int municionMaxima;

    protected ArmaDeFuego() {
        super();
        this.municionActual = 30;
        this.municionMaxima = 30;
    }

    public ArmaDeFuego(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad) {
        super(nombre, id, precio);
        setDano(dano);
        setPrecision(precision);
        setRecarga(recarga);
        setVelocidad(velocidad);
        this.municionActual = 30;
        this.municionMaxima = 30;
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

    public int getMunicionActual() { return municionActual; }

    public void setMunicionActual(int municionActual) {
        if (municionActual < 0) {
            throw new CargaMunicionException("La munición actual no puede ser negativa: " + municionActual);
        }
        this.municionActual = municionActual;
    }

    public int getMunicionMaxima() { return municionMaxima; }

    public void setMunicionMaxima(int municionMaxima) {
        if (municionMaxima <= 0) {
            throw new CargaMunicionException("La munición máxima debe ser mayor a 0: " + municionMaxima);
        }
        this.municionMaxima = municionMaxima;
    }

    public int calcularDano() {
        return dano;
    }

    public int calcularDano(int distancia) {
        if (distancia < 0) {
            throw new ArmaInvalidaException("La distancia no puede ser negativa: " + distancia);
        }
        return Math.max(1, dano - distancia / 10);
    }

    public int calcularDano(int distancia, boolean disparoCritico) {
        int danoBase = calcularDano(distancia);
        return disparoCritico ? danoBase * 2 : danoBase;
    }

    public int dispararA(Arma objetivo, int distancia, boolean disparoCritico) {
        if (objetivo == null) {
            throw new ArmaInvalidaException("El objetivo del disparo no puede ser nulo");
        }
        if (municionActual <= 0) {
            throw new CargaMunicionException("No hay munición disponible para disparar");
        }
        int danoAplicado = calcularDano(distancia, disparoCritico);
        objetivo.recibirDano(danoAplicado);
        municionActual--;
        return danoAplicado;
    }

    public int recargar(int cantidad) {
        if (cantidad <= 0) {
            throw new CargaMunicionException("La cantidad a recargar debe ser mayor a 0: " + cantidad);
        }
        int nuevaMunicion = municionActual + cantidad;
        if (nuevaMunicion > municionMaxima) {
            throw new CargaMunicionException(
                    "Recarga inválida: munición actual (" + municionActual
                            + ") + cantidad (" + cantidad
                            + ") supera la capacidad máxima (" + municionMaxima + ")");
        }
        municionActual = nuevaMunicion;
        return municionActual;
    }

    @Override
    public String describir() {
        return "nombre=" + getNombre()
                + ", id=" + getId()
                + ", precio=" + getPrecio()
                + ", durabilidad=" + getDurabilidad()
                + ", dano=" + dano
                + ", precision=" + precision
                + ", recarga=" + recarga
                + ", velocidad=" + velocidad
                + ", municionActual=" + municionActual
                + ", municionMaxima=" + municionMaxima;
    }
}
