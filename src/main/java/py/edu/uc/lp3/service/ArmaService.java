package py.edu.uc.lp3.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import py.edu.uc.lp3.entity.ArmaEntity;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
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
        return armaRepository.save(arma);
    }

    public ArmaEntity update(Long id, ArmaEntity arma) {
        if (id == null || id <= 0) {
            throw new ArmaInvalidaException("El id debe ser mayor a 0");
        }
        validar(arma);
        ArmaEntity existente = armaRepository.findById(id)
                .orElseThrow(() -> new ArmaInvalidaException("Arma no encontrada con id: " + id));
        existente.setNombre(arma.getNombre());
        existente.setPrecio(arma.getPrecio());
        existente.setTipo(arma.getTipo());
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
}
