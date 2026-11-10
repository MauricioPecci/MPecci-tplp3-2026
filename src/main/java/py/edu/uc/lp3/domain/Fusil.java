package py.edu.uc.lp3.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;

@Entity
@DiscriminatorValue("FUSIL")
public class Fusil extends ArmaDeFuego implements Equipable {

    @Column(name = "fusil_automatica")
    private boolean automatica;

    private int mira;

    private int retroceso;

    @Column(name = "fusil_silenciador")
    private boolean silenciador;

    public Fusil() {
        super();
        setMira(1);
        setRetroceso(0);
    }

    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    public Fusil(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                 boolean automatica, int mira, int retroceso, boolean silenciador) {
        super(nombre, id, precio, dano, precision, recarga, velocidad);
        this.automatica = automatica;
        setMira(mira);
        setRetroceso(retroceso);
        this.silenciador = silenciador;
    }

    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    public Fusil(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                 boolean automatica) {
        this(nombre, id, precio, dano, precision, recarga, velocidad, automatica, 1, 0, false);
    }

    @Override
    public String ranura() {
        return "primaria";
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
