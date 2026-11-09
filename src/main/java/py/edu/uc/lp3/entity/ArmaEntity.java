package py.edu.uc.lp3.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "armas")
public class ArmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private double precio;

    private String tipo;

    private int dano;

    private int municionActual;

    private int municionMaxima;

    public ArmaEntity() {
    }

    public ArmaEntity(String nombre, double precio, String tipo) {
        this.nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;
    }

    public ArmaEntity(String nombre, double precio, String tipo, int dano, int municionActual, int municionMaxima) {
        this.nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;
        this.dano = dano;
        this.municionActual = municionActual;
        this.municionMaxima = municionMaxima;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDano() {
        return dano;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public int getMunicionActual() {
        return municionActual;
    }

    public void setMunicionActual(int municionActual) {
        this.municionActual = municionActual;
    }

    public int getMunicionMaxima() {
        return municionMaxima;
    }

    public void setMunicionMaxima(int municionMaxima) {
        this.municionMaxima = municionMaxima;
    }
}
