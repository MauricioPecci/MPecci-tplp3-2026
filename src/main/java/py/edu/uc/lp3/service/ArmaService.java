package py.edu.uc.lp3.service;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.ArmaDeFuego;
import py.edu.uc.lp3.domain.Arrojadiza;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.ArmaNoEncontradaException;
import py.edu.uc.lp3.exceptions.CargaMunicionException;
import py.edu.uc.lp3.exceptions.DanoInvalidoException;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.rest.dto.SimulacionRequest;
import py.edu.uc.lp3.rest.dto.SimulacionResponse;

@Service
public class ArmaService {

    private final ArmaRepository armaRepository;

    public ArmaService(ArmaRepository armaRepository) {
        this.armaRepository = armaRepository;
    }

    public List<Arma> findAll(String nombre, Double precioMax) {
        if (nombre != null && !nombre.isBlank()) {
            return armaRepository.findByNombreContainingIgnoreCase(nombre);
        }
        if (precioMax != null) {
            return armaRepository.findByPrecioLessThanEqual(precioMax);
        }
        return armaRepository.findAll();
    }

    public Arma findById(Integer id) {
        if (id == null || id <= 0) {
            throw new ArmaInvalidaException("El id debe ser mayor a 0");
        }
        return armaRepository.findById(id)
                .orElseThrow(() -> new ArmaNoEncontradaException("Arma no encontrada con id: " + id));
    }

    @Transactional
    public Arma create(Arma arma) {
        if (arma == null) {
            throw new ArmaInvalidaException("El arma no puede ser nula");
        }
        if (arma.getId() != 0) {
            throw new ArmaInvalidaException("El id lo asigna la persistencia y no debe enviarse");
        }
        validar(arma);
        return armaRepository.save(arma);
    }

    @Transactional
    public Arma update(Integer id, Arma datos) {
        Arma existente = findById(id);
        if (datos == null) {
            throw new ArmaInvalidaException("El arma no puede ser nula");
        }
        if (!existente.getClass().equals(datos.getClass())) {
            throw new ArmaInvalidaException(
                    "No se puede cambiar el tipo del arma: se esperaba "
                            + existente.getClass().getSimpleName());
        }
        BeanUtils.copyProperties(datos, existente, "id");
        validar(existente);
        return armaRepository.save(existente);
    }

    @Transactional
    public void delete(Integer id) {
        Arma arma = findById(id);
        armaRepository.delete(arma);
    }

    @Transactional
    public Arma aplicarDano(Integer id, int dano) {
        Arma arma = findById(id);
        arma.recibirDano(dano);
        return armaRepository.save(arma);
    }

    @Transactional
    public Arma cargarMunicion(Integer id, int cantidad) {
        Arma arma = findById(id);
        if (!(arma instanceof ArmaDeFuego fuego)) {
            throw new ArmaInvalidaException("Solo las armas de fuego pueden recargar munición");
        }
        fuego.recargar(cantidad);
        return armaRepository.save(arma);
    }

    @Transactional
    public SimulacionResponse simular(SimulacionRequest request) {
        if (request == null) {
            throw new ArmaInvalidaException("La simulación no puede ser nula");
        }

        Arma atacante = findById(request.atacanteId());
        Arma objetivo = findById(request.objetivoId());
        Arma recargador = findById(request.recargadorId());

        if (atacante.getId() == objetivo.getId()
                || atacante.getId() == recargador.getId()
                || objetivo.getId() == recargador.getId()) {
            throw new ArmaInvalidaException("La simulación requiere tres armas distintas");
        }
        if (!(atacante instanceof ArmaDeFuego fuegoAtacante)) {
            throw new ArmaInvalidaException("El atacante debe ser un arma de fuego");
        }
        if (!(recargador instanceof ArmaDeFuego fuegoRecargador)) {
            throw new ArmaInvalidaException("El recargador debe ser un arma de fuego");
        }

        int atacanteMunicionAntes = fuegoAtacante.getMunicionActual();
        int objetivoDurabilidadAntes = objetivo.getDurabilidad();
        int recargadorMunicionAntes = fuegoRecargador.getMunicionActual();

        int danoAplicado = fuegoAtacante.dispararA(objetivo, request.distancia(), request.critico());
        int recargadorMunicionDespues = fuegoRecargador.recargar(request.municion());

        armaRepository.save(atacante);
        armaRepository.save(objetivo);
        armaRepository.save(recargador);

        return new SimulacionResponse(
                atacante.getClass().getSimpleName(),
                atacanteMunicionAntes,
                fuegoAtacante.getMunicionActual(),
                objetivo.getClass().getSimpleName(),
                objetivoDurabilidadAntes,
                objetivo.getDurabilidad(),
                recargador.getClass().getSimpleName(),
                recargadorMunicionAntes,
                recargadorMunicionDespues,
                danoAplicado,
                request.municion());
    }

    private void validar(Arma arma) {
        if (arma == null) {
            throw new ArmaInvalidaException("El arma no puede ser nula");
        }
        if (arma.getNombre() == null || arma.getNombre().isBlank()) {
            throw new ArmaInvalidaException("El nombre del arma no puede estar vacío");
        }
        if (arma.getPrecio() < 0) {
            throw new ArmaInvalidaException("El precio no puede ser negativo: " + arma.getPrecio());
        }
        if (arma.getDurabilidad() < 0) {
            throw new ArmaInvalidaException("La durabilidad no puede ser negativa: " + arma.getDurabilidad());
        }
        if (arma instanceof ArmaDeFuego fuego) {
            if (fuego.getDano() < 0) {
                throw new DanoInvalidoException("El daño no puede ser negativo: " + fuego.getDano());
            }
            if (fuego.getMunicionActual() < 0) {
                throw new CargaMunicionException(
                        "La munición actual no puede ser negativa: " + fuego.getMunicionActual());
            }
            if (fuego.getMunicionMaxima() <= 0) {
                throw new CargaMunicionException(
                        "La munición máxima debe ser mayor a 0: " + fuego.getMunicionMaxima());
            }
            if (fuego.getMunicionActual() > fuego.getMunicionMaxima()) {
                throw new CargaMunicionException(
                        "La munición actual (" + fuego.getMunicionActual()
                                + ") no puede superar la munición máxima (" + fuego.getMunicionMaxima() + ")");
            }
        }
        if (arma instanceof Arrojadiza arrojadiza) {
            if (arrojadiza.getRadio() <= 0) {
                throw new ArmaInvalidaException(
                        "El radio de cobertura debe ser mayor a 0: " + arrojadiza.getRadio());
            }
            if (arrojadiza.getDistancia() <= 0) {
                throw new ArmaInvalidaException(
                        "La distancia de lanzamiento debe ser mayor a 0: " + arrojadiza.getDistancia());
            }
            if (arrojadiza.getDuracion() <= 0) {
                throw new ArmaInvalidaException(
                        "La duración debe ser mayor a 0: " + arrojadiza.getDuracion());
            }
        }
    }
}
