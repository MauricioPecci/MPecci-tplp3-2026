package py.edu.uc.lp3.domain;

public class Granada extends Arrojadiza implements Equipable {
    private int dano;
    private boolean aturdimiento;

    public Granada(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion,
                   int dano, boolean aturdimiento) {
        super(nombre, id, precio, tipo, radio, distancia, duracion);
        this.dano = dano;
        this.aturdimiento = aturdimiento;
    }

    @Override
    public String ranura() {
        return "explosiva";
    }

    @Override
    public String mensajeDeEquipado() {
        return "No se equipa: se lleva en la ranura explosiva y se lanza con la tecla G.";
    }

    @Override
    public String describir() {
        return "Granada[" + super.describir()
                + ", dano=" + dano
                + ", aturdimiento=" + aturdimiento + "]";
    }

    public int getDano() { return dano; }
    public void setDano(int dano) { this.dano = dano; }

    public boolean isAturdimiento() { return aturdimiento; }
    public void setAturdimiento(boolean aturdimiento) { this.aturdimiento = aturdimiento; }
}
