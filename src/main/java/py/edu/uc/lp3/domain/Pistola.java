package py.edu.uc.lp3.domain;

public class Pistola extends ArmaDeFuego {
    private boolean automatica;
    private boolean silenciador;

    public Pistola(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                   boolean automatica, boolean silenciador) {
        super(nombre, id, precio, dano, precision, recarga, velocidad);
        this.automatica = automatica;
        this.silenciador = silenciador;
    }

    @Override
    public String describir() {
        return "Pistola[" + super.describir()
                + ", automatica=" + automatica
                + ", silenciador=" + silenciador + "]";
    }

    public boolean isAutomatica() { return automatica; }
    public void setAutomatica(boolean automatica) { this.automatica = automatica; }

    public boolean isSilenciador() { return silenciador; }
    public void setSilenciador(boolean silenciador) { this.silenciador = silenciador; }
}
