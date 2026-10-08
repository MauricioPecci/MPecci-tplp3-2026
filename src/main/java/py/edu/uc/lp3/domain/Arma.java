package py.edu.uc.lp3.domain;

import py.edu.uc.lp3.exceptions.ArmaInvalidaException;

public abstract class Arma {
    private String nombre;
    private int id;
    private double precio;

    public Arma(String nombre, int id, double precio) {
        setNombre(nombre);
        setId(id);
        setPrecio(precio);
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ArmaInvalidaException("El nombre del arma no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public int getId() { return id; }

    public void setId(int id) {
        if (id <= 0) {
            throw new ArmaInvalidaException("El id debe ser mayor a 0: " + id);
        }
        this.id = id;
    }

    public double getPrecio() { return precio; }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new ArmaInvalidaException("El precio no puede ser negativo: " + precio);
        }
        this.precio = precio;
    }

    public abstract String describir();
}
