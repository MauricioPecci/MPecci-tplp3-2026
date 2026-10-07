package armas;

public class Flash extends Arrojadiza {
    private int intensidad;
    private double duracionCeguera;

    public Flash(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion,
                 int intensidad, double duracionCeguera) {
        super(nombre, id, precio, tipo, radio, distancia, duracion);
        this.intensidad = intensidad;
        this.duracionCeguera = duracionCeguera;
    }

    public int getIntensidad() { return intensidad; }
    public void setIntensidad(int intensidad) { this.intensidad = intensidad; }

    public double getDuracionCeguera() { return duracionCeguera; }
    public void setDuracionCeguera(double duracionCeguera) { this.duracionCeguera = duracionCeguera; }
}