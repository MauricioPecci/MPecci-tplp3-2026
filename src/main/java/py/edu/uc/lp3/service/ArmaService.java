package py.edu.uc.lp3.service;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.ArmaDeFuego;
import py.edu.uc.lp3.domain.Arrojadiza;
import py.edu.uc.lp3.domain.Escopeta;
import py.edu.uc.lp3.domain.Flash;
import py.edu.uc.lp3.domain.Francotirador;
import py.edu.uc.lp3.domain.Fusil;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.ArmaNoEncontradaException;
import py.edu.uc.lp3.exceptions.CargaMunicionException;
import py.edu.uc.lp3.exceptions.DanoInvalidoException;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.rest.dto.DanoResponse;
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
    public DanoResponse aplicarDano(Integer id, int dano) {
        if (dano < 0) {
            throw new DanoInvalidoException("El daño aplicado no puede ser negativo: " + dano);
        }
        Arma arma = findById(id);
        int danoAplicado = arma.recibirDano(dano);
        boolean destruida = arma.getDurabilidad() == 0;
        if (destruida) {
            armaRepository.delete(arma);
        } else {
            armaRepository.save(arma);
        }
        return new DanoResponse(arma.getId(), arma.getNombre(), arma.getDurabilidad(), danoAplicado, destruida);
    }

    @Transactional
    public Arma cargarMunicion(Integer id, int cantidad) {
        if (cantidad < 0) {
            throw new CargaMunicionException("La cantidad a recargar no puede ser negativa: " + cantidad);
        }
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
        if (request.municion() < 0) {
            throw new CargaMunicionException(
                    "La munición a recargar no puede ser negativa: " + request.municion());
        }
        if (request.distancia() < 0) {
            throw new ArmaInvalidaException("La distancia no puede ser negativa: " + request.distancia());
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
        boolean objetivoDestruido = objetivo.getDurabilidad() == 0;

        armaRepository.save(atacante);
        if (objetivoDestruido) {
            armaRepository.delete(objetivo);
        } else {
            armaRepository.save(objetivo);
        }
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
                request.municion(),
                objetivoDestruido);
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
            validarNoNegativo("La precisión", fuego.getPrecision());
            if (fuego.getPrecision() > 100) {
                throw new ArmaInvalidaException("La precisión no puede superar 100: " + fuego.getPrecision());
            }
            if (fuego.getRecarga() <= 0) {
                throw new ArmaInvalidaException("La recarga debe ser mayor a 0: " + fuego.getRecarga());
            }
            validarNoNegativo("La velocidad de disparo", fuego.getVelocidad());
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
        if (arma instanceof Fusil fusil) {
            validarNoNegativo("La mira", fusil.getMira());
            validarNoNegativo("El retroceso", fusil.getRetroceso());
        }
        if (arma instanceof Escopeta escopeta) {
            validarNoNegativo("El perdigón", escopeta.getPerdigon());
            validarNoNegativo("La dispersión", escopeta.getDispersion());
        }
        if (arma instanceof Francotirador francotirador) {
            validarNoNegativo("El alcance", francotirador.getAlcance());
            validarNoNegativo("El zoom", francotirador.getZoom());
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
        if (arma instanceof Granada granada) {
            if (granada.getDano() < 0) {
                throw new DanoInvalidoException("El daño explosivo no puede ser negativo: " + granada.getDano());
            }
        }
        if (arma instanceof Flash flash) {
            validarNoNegativo("La intensidad", flash.getIntensidad());
            validarNoNegativo("La duración de ceguera", flash.getDuracionCeguera());
        }
    }

    private void validarNoNegativo(String campo, double valor) {
        if (valor < 0) {
            throw new ArmaInvalidaException(campo + " no puede ser negativo: " + valor);
        }
    }
}
