package py.edu.uc.lp3.MPecci.Cs2.armas;

public class Humo extends Arrojadiza {

    public Humo(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion) {
        super(nombre, id, precio, tipo, radio, distancia, duracion);
    }

    public Humo(String nombre, int id, double precio) {
        this(nombre, id, precio, "humo", 6.0, 25.0, 18.0);
    }

    public Humo(String nombre, int id, double precio, double radio) {
        this(nombre, id, precio, "humo", radio, 25.0, 18.0);
    }

    public double areaCobertura() {
        return Math.PI * getRadio() * getRadio();
    }

    public double areaCobertura(double radioExtra) {
        double radioTotal = getRadio() + radioExtra;
        return Math.PI * radioTotal * radioTotal;
    }

    @Override
    public String describir() {
        return "Humo[" + super.describir()
                + ", tipo=" + getTipo()
                + ", radio=" + getRadio()
                + ", distancia=" + getDistancia()
                + ", duracion=" + getDuracion() + "]";
    }
}
