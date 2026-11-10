package py.edu.uc.lp3.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ESCOPETA")
public class Escopeta extends ArmaDeFuego {
    private int perdigon;
    private double dispersion;

    public Escopeta() {
        super();
        setPerdigon(8);
        setDispersion(4.5);
    }

    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    public Escopeta(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                    int perdigon, double dispersion) {
        super(nombre, id, precio, dano, precision, recarga, velocidad);
        this.perdigon = perdigon;
        this.dispersion = dispersion;
    }

    @Override
    public String describir() {
        return "Escopeta[" + super.describir()
                + ", perdigon=" + perdigon
                + ", dispersion=" + dispersion + "]";
    }

    public int getPerdigon() { return perdigon; }
    public void setPerdigon(int perdigon) { this.perdigon = perdigon; }

    public double getDispersion() { return dispersion; }
    public void setDispersion(double dispersion) { this.dispersion = dispersion; }
}
