package py.edu.uc.lp3.domain;

import jakarta.persistence.MappedSuperclass;

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
    public void setDano(int dano) { this.dano = dano; }

    public int getPrecision() { return precision; }
    public void setPrecision(int precision) { this.precision = precision; }

    public double getRecarga() { return recarga; }
    public void setRecarga(double recarga) { this.recarga = recarga; }

    public double getVelocidad() { return velocidad; }
    public void setVelocidad(double velocidad) { this.velocidad = velocidad; }

    public int getMunicionActual() { return municionActual; }
    public void setMunicionActual(int municionActual) { this.municionActual = municionActual; }

    public int getMunicionMaxima() { return municionMaxima; }
    public void setMunicionMaxima(int municionMaxima) { this.municionMaxima = municionMaxima; }

    public int calcularDano() {
        return dano;
    }

    public int calcularDano(int distancia) {
        return Math.max(1, dano - distancia / 10);
    }

    public int calcularDano(int distancia, boolean disparoCritico) {
        int danoBase = calcularDano(distancia);
        return disparoCritico ? danoBase * 2 : danoBase;
    }

    public int dispararA(Arma objetivo, int distancia, boolean disparoCritico) {
        int danoCalculado = calcularDano(distancia, disparoCritico);
        int danoAplicado = objetivo.recibirDano(danoCalculado);
        municionActual--;
        return danoAplicado;
    }

    public int recargar(int cantidad) {
        municionActual = municionMaxima;
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
