package py.edu.uc.lp3.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import py.edu.uc.lp3.repository.ArmaRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ArmaCrudControllerTest {

    private static final String FUSIL_JSON = """
            {
              "tipoArma": "FUSIL",
              "nombre": "AK-47",
              "precio": 2700,
              "durabilidad": 100,
              "dano": 36,
              "precision": 71,
              "recarga": 2.4,
              "velocidad": 600,
              "municionActual": 30,
              "municionMaxima": 30,
              "automatica": true,
              "mira": 2,
              "retroceso": 3,
              "silenciador": false
            }
            """;

    private static final String PISTOLA_JSON = """
            {
              "tipoArma": "PISTOLA",
              "nombre": "USP-S",
              "precio": 200,
              "durabilidad": 100,
              "dano": 35,
              "precision": 70,
              "recarga": 1.5,
              "velocidad": 352,
              "municionActual": 12,
              "municionMaxima": 20,
              "automatica": false,
              "silenciador": true
            }
            """;

    private static final String HUMO_JSON = """
            {
              "tipoArma": "HUMO",
              "nombre": "Humo de cortina",
              "precio": 300,
              "durabilidad": 100,
              "tipo": "humo",
              "radio": 6.0,
              "distancia": 25.0,
              "duracion": 18.0
            }
            """;

    private static final String GRANADA_JSON = """
            {
              "tipoArma": "GRANADA",
              "nombre": "Granada HE",
              "precio": 300,
              "durabilidad": 100,
              "tipo": "explosiva",
              "radio": 4.0,
              "distancia": 30.0,
              "duracion": 1.0,
              "dano": 98,
              "aturdimiento": false
            }
            """;

    private static final String ESCOPETA_JSON = """
            {
              "tipoArma": "ESCOPETA",
              "nombre": "XM1014",
              "precio": 2000,
              "durabilidad": 100,
              "dano": 20,
              "precision": 55,
              "recarga": 3.0,
              "velocidad": 171,
              "municionActual": 6,
              "municionMaxima": 30,
              "perdigon": 8,
              "dispersion": 4.5
            }
            """;

    private static final String FLASH_JSON = """
            {
              "tipoArma": "FLASH",
              "nombre": "Flashbang",
              "precio": 200,
              "durabilidad": 100,
              "tipo": "cegadora",
              "radio": 5.0,
              "distancia": 20.0,
              "duracion": 2.0,
              "intensidad": 100,
              "duracionCeguera": 4.5
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArmaRepository armaRepository;

    @BeforeEach
    void limpiarBase() {
        armaRepository.deleteAll();
    }

    @Test
    void crearDevuelve201ConIdYSubtipo() throws Exception {
        mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FUSIL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.tipoArma").value("FUSIL"))
                .andExpect(jsonPath("$.nombre").value("AK-47"));
    }

    @Test
    void listarDevuelveLosSubtiposPersistidos() throws Exception {
        crear(FUSIL_JSON);
        crear(PISTOLA_JSON);
        crear(HUMO_JSON);

        mockMvc.perform(get("/api/armas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].tipoArma").exists())
                .andExpect(jsonPath("$[1].tipoArma").exists())
                .andExpect(jsonPath("$[2].tipoArma").exists());
    }

    @Test
    void getPorIdDevuelveElRecursoY404SiNoExiste() throws Exception {
        int id = crear(FUSIL_JSON);

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.tipoArma").value("FUSIL"));

        mockMvc.perform(get("/api/armas/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.tipo").value("ARMA_NO_ENCONTRADA"));
    }

    @Test
    void putActualizaYPersisteElCambio() throws Exception {
        int id = crear(FUSIL_JSON);
        String actualizado = """
                {
                  "tipoArma": "FUSIL",
                  "nombre": "M4A1-S",
                  "precio": 3100,
                  "durabilidad": 90,
                  "dano": 33,
                  "precision": 66,
                  "recarga": 3.1,
                  "velocidad": 585,
                  "municionActual": 25,
                  "municionMaxima": 25,
                  "automatica": false,
                  "mira": 4,
                  "retroceso": 2,
                  "silenciador": true
                }
                """;

        mockMvc.perform(put("/api/armas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("M4A1-S"));

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("M4A1-S"))
                .andExpect(jsonPath("$.precio").value(3100.0));
    }

    @Test
    void deleteEliminaYElGetPosteriorEs404() throws Exception {
        int id = crear(FUSIL_JSON);

        mockMvc.perform(delete("/api/armas/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.tipo").value("ARMA_NO_ENCONTRADA"));
    }

    @Test
    void postInvalidoNoPersisteNada() throws Exception {
        String invalido = """
                {
                  "tipoArma": "FUSIL",
                  "nombre": "AK-47",
                  "precio": -1,
                  "durabilidad": 100,
                  "dano": 36,
                  "precision": 71,
                  "recarga": 2.4,
                  "velocidad": 600,
                  "municionActual": 30,
                  "municionMaxima": 30,
                  "automatica": true,
                  "mira": 2,
                  "retroceso": 3,
                  "silenciador": false
                }
                """;

        mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("ARMA_INVALIDA"));

        mockMvc.perform(get("/api/armas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void postConDanoNegativoDeGranadaDevuelve400YNoPersiste() throws Exception {
        String invalido = GRANADA_JSON.replace("\"dano\": 98", "\"dano\": -1");

        mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("DANO_INVALIDO"));

        mockMvc.perform(get("/api/armas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void putConDanoNegativoDeGranadaDevuelve400YConservaElValorPersistido() throws Exception {
        int id = crear(GRANADA_JSON);
        String invalido = GRANADA_JSON.replace("\"dano\": 98", "\"dano\": -5");

        mockMvc.perform(put("/api/armas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("DANO_INVALIDO"));

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dano").value(98));
    }

    @Test
    void postConCantidadesNegativasDeClasesHijasDevuelve400() throws Exception {
        mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ESCOPETA_JSON.replace("\"perdigon\": 8", "\"perdigon\": -1")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("ARMA_INVALIDA"));

        mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FLASH_JSON.replace("\"intensidad\": 100", "\"intensidad\": -1")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("ARMA_INVALIDA"));

        mockMvc.perform(get("/api/armas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void aplicarDanoNegativoDevuelve400YNoCambiaLaDurabilidad() throws Exception {
        int id = crear(FUSIL_JSON);

        mockMvc.perform(post("/api/armas/{id}/dano", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dano\": -5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("DANO_INVALIDO"));

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.durabilidad").value(100));
    }

    @Test
    void cargarMunicionConCantidadNegativaDevuelve400YNoCambiaLaMunicion() throws Exception {
        int id = crear(FUSIL_JSON);

        mockMvc.perform(post("/api/armas/{id}/cargar-municion", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\": -5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("CARGA_MUNICION_INVALIDA"));

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.municionActual").value(30));
    }

    @Test
    void simulacionFelizCambiaElEstadoDeLasTresClasesYPersiste() throws Exception {
        int atacante = crear(FUSIL_JSON);
        int objetivo = crear(HUMO_JSON);
        int recargador = crear(PISTOLA_JSON);

        String simulacion = """
                {
                  "atacanteId": %d,
                  "objetivoId": %d,
                  "recargadorId": %d,
                  "distancia": 0,
                  "critico": false,
                  "municion": 5
                }
                """.formatted(atacante, objetivo, recargador);

        mockMvc.perform(post("/api/armas/simulacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(simulacion))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.atacanteClase").value("Fusil"))
                .andExpect(jsonPath("$.objetivoClase").value("Humo"))
                .andExpect(jsonPath("$.recargadorClase").value("Pistola"))
                .andExpect(jsonPath("$.danoAplicado").value(36))
                .andExpect(jsonPath("$.atacanteMunicionDespues").value(29))
                .andExpect(jsonPath("$.objetivoDurabilidadDespues").value(64))
                .andExpect(jsonPath("$.recargadorMunicionDespues").value(17))
                .andExpect(jsonPath("$.objetivoDestruido").value(false));

        mockMvc.perform(get("/api/armas/{id}", atacante))
                .andExpect(jsonPath("$.municionActual").value(29));
        mockMvc.perform(get("/api/armas/{id}", objetivo))
                .andExpect(jsonPath("$.durabilidad").value(64));
        mockMvc.perform(get("/api/armas/{id}", recargador))
                .andExpect(jsonPath("$.municionActual").value(17));
    }

    @Test
    void simulacionConDanoExcesivoDestruyeObjetivoYClampaDurabilidad() throws Exception {
        int atacante = crear(FUSIL_JSON);
        int objetivo = crear("""
                {
                  "tipoArma": "HUMO",
                  "nombre": "Humo frágil",
                  "precio": 300,
                  "durabilidad": 10,
                  "tipo": "humo",
                  "radio": 6.0,
                  "distancia": 25.0,
                  "duracion": 18.0
                }
                """);
        int recargador = crear(PISTOLA_JSON);

        String simulacion = """
                {
                  "atacanteId": %d,
                  "objetivoId": %d,
                  "recargadorId": %d,
                  "distancia": 0,
                  "critico": false,
                  "municion": 5
                }
                """.formatted(atacante, objetivo, recargador);

        mockMvc.perform(post("/api/armas/simulacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(simulacion))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.danoAplicado").value(10))
                .andExpect(jsonPath("$.objetivoDurabilidadAntes").value(10))
                .andExpect(jsonPath("$.objetivoDurabilidadDespues").value(0))
                .andExpect(jsonPath("$.objetivoDestruido").value(true))
                .andExpect(jsonPath("$.atacanteMunicionDespues").value(29))
                .andExpect(jsonPath("$.recargadorMunicionDespues").value(17));

        mockMvc.perform(get("/api/armas/{id}", atacante))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.municionActual").value(29));
        mockMvc.perform(get("/api/armas/{id}", objetivo))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.tipo").value("ARMA_NO_ENCONTRADA"));
        mockMvc.perform(get("/api/armas/{id}", recargador))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.municionActual").value(17));
    }

    @Test
    void simulacionConCargaExcesivaDevuelveHandlerDeMunicion() throws Exception {
        int atacante = crear(FUSIL_JSON);
        int objetivo = crear(HUMO_JSON);
        int recargador = crear(PISTOLA_JSON);

        String simulacion = """
                {
                  "atacanteId": %d,
                  "objetivoId": %d,
                  "recargadorId": %d,
                  "distancia": 0,
                  "critico": false,
                  "municion": 50
                }
                """.formatted(atacante, objetivo, recargador);

        mockMvc.perform(post("/api/armas/simulacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(simulacion))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("CARGA_MUNICION_INVALIDA"));
    }

    @Test
    void deleteInexistenteDevuelve404() throws Exception {
        mockMvc.perform(delete("/api/armas/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.tipo").value("ARMA_NO_ENCONTRADA"));
    }

    @Test
    void aplicarDanoDestruyeArmaYDevuelveCuerpoConDestruccion() throws Exception {
        int id = crear(HUMO_JSON);

        mockMvc.perform(post("/api/armas/{id}/dano", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dano\": 100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Humo de cortina"))
                .andExpect(jsonPath("$.durabilidad").value(0))
                .andExpect(jsonPath("$.danoAplicado").value(100))
                .andExpect(jsonPath("$.destruida").value(true));

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.tipo").value("ARMA_NO_ENCONTRADA"));
    }

    @Test
    void aplicarDanoExcesivoClampaADurabilidadYDestruyeArma() throws Exception {
        int id = crear(HUMO_JSON);

        mockMvc.perform(post("/api/armas/{id}/dano", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dano\": 150}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.durabilidad").value(0))
                .andExpect(jsonPath("$.danoAplicado").value(100))
                .andExpect(jsonPath("$.destruida").value(true));

        mockMvc.perform(get("/api/armas/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.tipo").value("ARMA_NO_ENCONTRADA"));
    }

    @Test
    void crearCadaSubtipoPorEndpointDevuelve201ConTipoCorrecto() throws Exception {
        mockMvc.perform(post("/api/armas/fusil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FUSIL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoArma").value("FUSIL"));

        mockMvc.perform(post("/api/armas/escopeta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ESCOPETA_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoArma").value("ESCOPETA"));

        mockMvc.perform(post("/api/armas/pistola")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PISTOLA_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoArma").value("PISTOLA"));

        String francotirador = """
                {
                  "tipoArma": "FRANCOTIRADOR",
                  "nombre": "AWP",
                  "precio": 4750,
                  "durabilidad": 100,
                  "dano": 115,
                  "precision": 85,
                  "recarga": 3.7,
                  "velocidad": 340,
                  "municionActual": 5,
                  "municionMaxima": 30,
                  "automatica": false,
                  "mira": 8,
                  "retroceso": 5,
                  "silenciador": true,
                  "alcance": 80.0,
                  "zoom": 4
                }
                """;
        mockMvc.perform(post("/api/armas/francotirador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(francotirador))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoArma").value("FRANCOTIRADOR"));

        mockMvc.perform(post("/api/armas/humo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HUMO_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoArma").value("HUMO"));

        mockMvc.perform(post("/api/armas/granada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GRANADA_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoArma").value("GRANADA"));

        mockMvc.perform(post("/api/armas/flash")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FLASH_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoArma").value("FLASH"));

        mockMvc.perform(get("/api/armas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }

    @Test
    void postConCuerpoInconsistenteDevuelve400YNo500() throws Exception {
        String invalido = """
                {
                  "tipoArma": "FUSIL",
                  "nombre": "Fusilako",
                  "precio": 0.1,
                  "durabilidad": 10,
                  "dano": 10,
                  "precision": 20,
                  "recarga": 0.1,
                  "velocidad": 0.1,
                  "municionActual": 1,
                  "municionMaxima": 10,
                  "perdigon": 1,
                  "dispersion": 0.1
                }
                """;

        mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/armas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private int crear(String json) throws Exception {
        String respuesta = mockMvc.perform(post("/api/armas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode nodo = objectMapper.readTree(respuesta);
        return nodo.get("id").asInt();
    }
}
