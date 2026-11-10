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
                .andExpect(jsonPath("$.recargadorMunicionDespues").value(17));

        mockMvc.perform(get("/api/armas/{id}", atacante))
                .andExpect(jsonPath("$.municionActual").value(29));
        mockMvc.perform(get("/api/armas/{id}", objetivo))
                .andExpect(jsonPath("$.durabilidad").value(64));
        mockMvc.perform(get("/api/armas/{id}", recargador))
                .andExpect(jsonPath("$.municionActual").value(17));
    }

    @Test
    void simulacionConDanoExcesivoDevuelveHandlerDeDano() throws Exception {
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tipo").value("DANO_INVALIDO"));
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
