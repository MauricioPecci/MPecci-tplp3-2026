package armas;

public class Escopeta extends ArmaDeFuego {
    private int perdigon;
    private double dispersion;

    public Escopeta(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad,
                    int perdigon, double dispersion) {
        super(nombre, id, precio, dano, precision, recarga, velocidad);
        this.perdigon = perdigon;
        this.dispersion = dispersion;
    }

    public int getPerdigon() { return perdigon; }
    public void setPerdigon(int perdigon) { this.perdigon = perdigon; }

    public double getDispersion() { return dispersion; }
    public void setDispersion(double dispersion) { this.dispersion = dispersion; }
}