package py.edu.uc.lp3.domain;

public class Granada extends Arrojadiza {
    private int dano;
    private boolean aturdimiento;

    public Granada(String nombre, int id, double precio, String tipo, double radio, double distancia, double duracion,
                   int dano, boolean aturdimiento) {
        super(nombre, id, precio, tipo, radio, distancia, duracion);
        this.dano = dano;
        this.aturdimiento = aturdimiento;
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
