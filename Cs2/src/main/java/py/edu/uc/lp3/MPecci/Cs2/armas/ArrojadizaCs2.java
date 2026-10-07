package armas;

public abstract class Arrojadiza extends Arma {
    private String tipo;
    private double radio;
    private double distancia;
    private double duracion;

    public Arrojadiza(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion) {
        super(nombre, id, precio);
        this.tipo = tipo;
        this.radio = radio;
        this.distancia = distancia;
        this.duracion = duracion;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getRadio() { return radio; }
    public void setRadio(double radio) { this.radio = radio; }

    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) { this.distancia = distancia; }

    public double getDuracion() { return duracion; }
    public void setDuracion(double duracion) { this.duracion = duracion; }
}