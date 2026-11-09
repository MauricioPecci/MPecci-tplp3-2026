package py.edu.uc.lp3.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import py.edu.uc.lp3.entity.ArmaEntity;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.CargaMunicionException;
import py.edu.uc.lp3.exceptions.DanoInvalidoException;
import py.edu.uc.lp3.repository.ArmaRepository;

@Service
public class ArmaService {

    private final ArmaRepository armaRepository;

    public ArmaService(ArmaRepository armaRepository) {
        this.armaRepository = armaRepository;
    }

    public List<ArmaEntity> findAll() {
        return armaRepository.findAll();
    }

    public Optional<ArmaEntity> findById(Long id) {
        if (id == null || id <= 0) {
            throw new ArmaInvalidaException("El id debe ser mayor a 0");
        }
        return armaRepository.findById(id);
    }

    public ArmaEntity create(ArmaEntity arma) {
        validar(arma);
        validarReglasNegocio(arma);
        return armaRepository.save(arma);
    }

    public ArmaEntity update(Long id, ArmaEntity arma) {
        if (id == null || id <= 0) {
            throw new ArmaInvalidaException("El id debe ser mayor a 0");
        }
        validar(arma);
        validarReglasNegocio(arma);
        ArmaEntity existente = armaRepository.findById(id)
                .orElseThrow(() -> new ArmaInvalidaException("Arma no encontrada con id: " + id));
        existente.setNombre(arma.getNombre());
        existente.setPrecio(arma.getPrecio());
        existente.setTipo(arma.getTipo());
        existente.setDano(arma.getDano());
        existente.setMunicionActual(arma.getMunicionActual());
        existente.setMunicionMaxima(arma.getMunicionMaxima());
        return armaRepository.save(existente);
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new ArmaInvalidaException("El id debe ser mayor a 0");
        }
        if (!armaRepository.existsById(id)) {
            throw new ArmaInvalidaException("Arma no encontrada con id: " + id);
        }
        armaRepository.deleteById(id);
    }

    public ArmaEntity aplicarDano(Long id, int dano) {
        if (dano < 0) {
            throw new DanoInvalidoException("El daño aplicado no puede ser negativo: " + dano);
        }
        ArmaEntity arma = armaRepository.findById(id)
                .orElseThrow(() -> new ArmaInvalidaException("Arma no encontrada con id: " + id));
        if (dano > arma.getMunicionMaxima() * 5) {
            throw new DanoInvalidoException("El daño excede el límite permitido para esta arma: " + dano);
        }
        arma.setDano(dano);
        return armaRepository.save(arma);
    }

    public ArmaEntity cargarMunicion(Long id, int cantidad) {
        if (cantidad <= 0) {
            throw new CargaMunicionException("La cantidad a cargar debe ser mayor a 0");
        }
        ArmaEntity arma = armaRepository.findById(id)
                .orElseThrow(() -> new ArmaInvalidaException("Arma no encontrada con id: " + id));
        int nuevaMunicion = arma.getMunicionActual() + cantidad;
        if (nuevaMunicion > arma.getMunicionMaxima()) {
            throw new CargaMunicionException(
                    "Carga inválida: munición actual (" + arma.getMunicionActual()
                            + ") + cantidad (" + cantidad
                            + ") supera la capacidad máxima (" + arma.getMunicionMaxima() + ")");
        }
        arma.setMunicionActual(nuevaMunicion);
        return armaRepository.save(arma);
    }

    private void validar(ArmaEntity arma) {
        if (arma == null) {
            throw new ArmaInvalidaException("El arma no puede ser nula");
        }
        if (arma.getNombre() == null || arma.getNombre().isBlank()) {
            throw new ArmaInvalidaException("El nombre del arma no puede estar vacío");
        }
        if (arma.getPrecio() < 0) {
            throw new ArmaInvalidaException("El precio no puede ser negativo: " + arma.getPrecio());
        }
        if (arma.getTipo() == null || arma.getTipo().isBlank()) {
            throw new ArmaInvalidaException("El tipo del arma no puede estar vacío");
        }
    }

    private void validarReglasNegocio(ArmaEntity arma) {
        if (arma.getDano() < 0) {
            throw new DanoInvalidoException("El daño no puede ser negativo: " + arma.getDano());
        }
        if (arma.getMunicionActual() < 0) {
            throw new CargaMunicionException("La munición actual no puede ser negativa: " + arma.getMunicionActual());
        }
        if (arma.getMunicionMaxima() <= 0) {
            throw new CargaMunicionException("La munición máxima debe ser mayor a 0: " + arma.getMunicionMaxima());
        }
        if (arma.getMunicionActual() > arma.getMunicionMaxima()) {
            throw new CargaMunicionException(
                    "La munición actual (" + arma.getMunicionActual()
                            + ") no puede superar la munición máxima (" + arma.getMunicionMaxima() + ")");
        }
    }
}
