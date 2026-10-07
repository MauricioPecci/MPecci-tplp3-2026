package armas;

public abstract class ArmaDeFuego extends Arma {
    private int dano;
    private int precision;
    private double recarga;
    private double velocidad;

    public ArmaDeFuego(String nombre, int id, double precio, int dano, int precision, double recarga, double velocidad) {
        super(nombre, id, precio);
        this.dano = dano;
        this.precision = precision;
        this.recarga = recarga;
        this.velocidad = velocidad;
    }

    public int getDano() { return dano; }
    public void setDano(int dano) { this.dano = dano; }

    public int getPrecision() { return precision; }
    public void setPrecision(int precision) { this.precision = precision; }

    public double getRecarga() { return recarga; }
    public void setRecarga(double recarga) { this.recarga = recarga; }

    public double getVelocidad() { return velocidad; }
    public void setVelocidad(double velocidad) { this.velocidad = velocidad; }
}