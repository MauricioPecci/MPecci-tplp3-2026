package py.edu.uc.lp3.domain;

import py.edu.uc.lp3.exceptions.ArmaInvalidaException;

public class Fusil extends ArmaDeFuego {
    private boolean automatica;
    private int mira;
    private int retroceso;
    private boolean silenciador;

    public Fusil(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                 boolean automatica, int mira, int retroceso, boolean silenciador) {
        super(nombre, id, precio, dano, precision, recarga, velocidad);
        this.automatica = automatica;
        setMira(mira);
        setRetroceso(retroceso);
        this.silenciador = silenciador;
    }

    public Fusil(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                 boolean automatica) {
        this(nombre, id, precio, dano, precision, recarga, velocidad, automatica, 1, 0, false);
    }

    public int calcularDano() {
        return getDano();
    }

    public int calcularDano(int distancia) {
        if (distancia < 0) {
            throw new ArmaInvalidaException("La distancia no puede ser negativa: " + distancia);
        }
        return Math.max(1, getDano() - distancia / 10);
    }

    public int calcularDano(int distancia, boolean disparoCritico) {
        int dano = calcularDano(distancia);
        return disparoCritico ? dano * 2 : dano;
    }

    @Override
    public String describir() {
        return "Fusil[" + super.describir()
                + ", automatica=" + automatica
                + ", mira=" + mira
                + ", retroceso=" + retroceso
                + ", silenciador=" + silenciador + "]";
    }

    public boolean isAutomatica() { return automatica; }
    public void setAutomatica(boolean automatica) { this.automatica = automatica; }

    public int getMira() { return mira; }

    public void setMira(int mira) {
        if (mira < 0) {
            throw new ArmaInvalidaException("La mira no puede ser negativa: " + mira);
        }
        this.mira = mira;
    }

    public int getRetroceso() { return retroceso; }

    public void setRetroceso(int retroceso) {
        if (retroceso < 0) {
            throw new ArmaInvalidaException("El retroceso no puede ser negativo: " + retroceso);
        }
        this.retroceso = retroceso;
    }

    public boolean isSilenciador() { return silenciador; }
    public void setSilenciador(boolean silenciador) { this.silenciador = silenciador; }
}
