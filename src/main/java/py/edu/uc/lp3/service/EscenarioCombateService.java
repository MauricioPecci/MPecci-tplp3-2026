package py.edu.uc.lp3.service;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
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
            throw new ArmaInvalidaException("El escenario de combate no puede ser nulo");
        }
        if (request.simulacionLocal() == null) {
            throw new ArmaInvalidaException("La simulación local es obligatoria");
        }
        if (request.explosionA2() == null) {
            throw new ArmaInvalidaException("La validación de explosión de A2 es obligatoria");
        }
        if (request.rafagaA3() == null) {
            throw new ArmaInvalidaException("La validación de ráfaga de A3 es obligatoria");
        }

        LOGGER.debug("Iniciando escenario de combate: A2 -> A3 -> simulación local");
        Map<String, Object> validacionA2 = companeroA2.validarExplosion(request.explosionA2());
        LOGGER.debug("A2 aceptó; solicitando validación a A3");
        Map<String, Object> validacionA3 = companeroA3.validarRafaga(request.rafagaA3());
        LOGGER.debug("A3 aceptó; ejecutando simulación local");
        SimulacionResponse simulacionLocal = armaService.simular(request.simulacionLocal());
        LOGGER.debug("Simulación local persistida correctamente");

        return new EscenarioCombateResponse(
                "Explosión validada por A2, ráfaga validada por A3 y simulación persistida en A1",
                simulacionLocal, validacionA2, validacionA3);
    }
}
