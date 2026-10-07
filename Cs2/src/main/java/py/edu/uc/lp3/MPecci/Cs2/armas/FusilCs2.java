package armas;

public class Fusil extends ArmaDeFuego {
    private boolean automatica;
    private int mira;
    private int retroceso;
    private boolean silenciador;

    public Fusil(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                 boolean automatica, int mira, int retroceso, boolean silenciador) {
        super(nombre, id, precio, dano, precision, recarga, velocidad);
        this.automatica = automatica;
        this.mira = mira;
        this.retroceso = retroceso;
        this.silenciador = silenciador;
    }

    public boolean isAutomatica() { return automatica; }
    public void setAutomatica(boolean automatica) { this.automatica = automatica; }

    public int getMira() { return mira; }
    public void setMira(int mira) { this.mira = mira; }

    public int getRetroceso() { return retroceso; }
    public void setRetroceso(int retroceso) { this.retroceso = retroceso; }

    public boolean isSilenciador() { return silenciador; }
    public void setSilenciador(boolean silenciador) { this.silenciador = silenciador; }
}