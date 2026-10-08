package py.edu.uc.lp3.domain;

public class Francotirador extends Fusil {
    private double alcance;
    private int zoom;

    public Francotirador(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                         boolean automatica, int mira, int retroceso, boolean silenciador,
                         double alcance, int zoom) {
        super(nombre, id, precio, dano, precision, recarga, velocidad, automatica, mira, retroceso, silenciador);
        this.alcance = alcance;
        this.zoom = zoom;
    }

    @Override
    public String describir() {
        return "Francotirador[" + super.describir()
                + ", alcance=" + alcance
                + ", zoom=" + zoom + "]";
    }

    public double getAlcance() { return alcance; }
    public void setAlcance(double alcance) { this.alcance = alcance; }

    public int getZoom() { return zoom; }
    public void setZoom(int zoom) { this.zoom = zoom; }
}
