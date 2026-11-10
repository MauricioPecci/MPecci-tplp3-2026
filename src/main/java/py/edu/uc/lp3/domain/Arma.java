package py.edu.uc.lp3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.DanoInvalidoException;

@Entity
@Table(name = "armas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_arma", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoArma")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Fusil.class, name = "FUSIL"),
        @JsonSubTypes.Type(value = Escopeta.class, name = "ESCOPETA"),
        @JsonSubTypes.Type(value = Pistola.class, name = "PISTOLA"),
        @JsonSubTypes.Type(value = Francotirador.class, name = "FRANCOTIRADOR"),
        @JsonSubTypes.Type(value = Humo.class, name = "HUMO"),
        @JsonSubTypes.Type(value = Granada.class, name = "GRANADA"),
        @JsonSubTypes.Type(value = Flash.class, name = "FLASH")
})
public abstract class Arma implements UIcapable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    private String nombre;

    private double precio;

    private int durabilidad = 100;

    protected Arma() {
    }

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

    public int getDurabilidad() { return durabilidad; }

    public void setDurabilidad(int durabilidad) {
        if (durabilidad < 0) {
            throw new ArmaInvalidaException("La durabilidad no puede ser negativa: " + durabilidad);
        }
        this.durabilidad = durabilidad;
    }

    public void recibirDano(int dano) {
        if (dano < 0) {
            throw new DanoInvalidoException("El daño aplicado no puede ser negativo: " + dano);
        }
        if (dano > durabilidad) {
            throw new DanoInvalidoException(
                    "El daño (" + dano + ") excede la durabilidad disponible (" + durabilidad + ")");
        }
        this.durabilidad -= dano;
    }

    public abstract String describir();

    @Override
    public String imagen() {
        return "recursos/imagenes/" + getClass().getSimpleName().toLowerCase() + ".png";
    }

    @Override
    public String sonido() {
        return "recursos/sonidos/" + getClass().getSimpleName().toLowerCase() + ".mp3";
    }
}
