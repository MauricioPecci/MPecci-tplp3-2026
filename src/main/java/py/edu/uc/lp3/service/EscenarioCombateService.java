package py.edu.uc.lp3.service;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.CompaneroNoDisponibleException;
import py.edu.uc.lp3.exceptions.ValidacionCompaneroRechazadaException;
import py.edu.uc.lp3.rest.dto.EscenarioCombateRequest;
import py.edu.uc.lp3.rest.dto.EscenarioCombateResponse;
import py.edu.uc.lp3.rest.dto.SimulacionResponse;

@Service
public class EscenarioCombateService {

    private static final Logger LOGGER = LogManager.getLogger(EscenarioCombateService.class);

    private final CompaneroA2Client companeroA2;
    private final CompaneroA3Client companeroA3;
    private final ArmaService armaService;

    public EscenarioCombateService(CompaneroA2Client companeroA2, CompaneroA3Client companeroA3,
            ArmaService armaService) {
        this.companeroA2 = companeroA2;
        this.companeroA3 = companeroA3;
        this.armaService = armaService;
    }

    public EscenarioCombateResponse ejecutar(EscenarioCombateRequest request) {
        if (request == null) {
            LOGGER.debug("Escenario rechazado: la solicitud es nula");
            throw new ArmaInvalidaException("El escenario de combate no puede ser nulo");
        }
        if (request.simulacionLocal() == null) {
            LOGGER.debug("Escenario rechazado: falta la simulación local");
            throw new ArmaInvalidaException("La simulación local es obligatoria");
        }
        if (request.explosionA2() == null) {
            LOGGER.debug("Escenario rechazado: falta la validación de A2");
            throw new ArmaInvalidaException("La validación de explosión de A2 es obligatoria");
        }
        if (request.rafagaA3() == null) {
            LOGGER.debug("Escenario rechazado: falta la validación de A3");
            throw new ArmaInvalidaException("La validación de ráfaga de A3 es obligatoria");
        }

        LOGGER.debug("Escenario iniciado: ids locales atacante={}, objetivo={}, recargador={}",
                request.simulacionLocal().atacanteId(), request.simulacionLocal().objetivoId(),
                request.simulacionLocal().recargadorId());
        LOGGER.debug("Reglas ajenas solicitadas: A2.granadaId={}, A3.tipo={}, A3.distancia={}, A3.veces={}",
                request.explosionA2().granadaId(), request.rafagaA3().tipo(),
                request.rafagaA3().distancia(), request.rafagaA3().veces());
        try {
            LOGGER.debug("Paso 1/3: validando explosión en A2");
            Map<String, Object> validacionA2 = companeroA2.validarExplosion(request.explosionA2());
            LOGGER.debug("Paso 1/3 completado: A2 aceptó; respuesta={}", validacionA2);

            LOGGER.debug("Paso 2/3: validando ráfaga en A3");
            Map<String, Object> validacionA3 = companeroA3.validarRafaga(request.rafagaA3());
            LOGGER.debug("Paso 2/3 completado: A3 aceptó; respuesta={}", validacionA3);

            LOGGER.debug("Paso 3/3: ejecutando simulación local en A1");
            SimulacionResponse simulacionLocal = armaService.simular(request.simulacionLocal());
            LOGGER.debug("Paso 3/3 completado: simulación persistida; danoAplicado={}, objetivoDestruido={}",
                    simulacionLocal.danoAplicado(), simulacionLocal.objetivoDestruido());

            EscenarioCombateResponse respuesta = new EscenarioCombateResponse(
                    "Explosión validada por A2, ráfaga validada por A3 y simulación persistida en A1",
                    simulacionLocal, validacionA2, validacionA3);
            LOGGER.debug("Escenario finalizado: 201 con validaciones de A2 y A3 y simulación local");
            return respuesta;
        } catch (ValidacionCompaneroRechazadaException e) {
            LOGGER.debug("Escenario rechazado por {} con código externo {}: {}",
                    e.getCompanero(), e.getEstadoExterno(), e.getCuerpoExterno());
            throw e;
        } catch (CompaneroNoDisponibleException e) {
            LOGGER.debug("Escenario abortado: {} no disponible - {}", e.getCompanero(), e.getMessage());
            throw e;
        } catch (ArmaInvalidaException e) {
            LOGGER.debug("Escenario rechazado por regla local de A1: {}", e.getMessage());
            throw e;
        }
    }
}
