package py.edu.uc.lp3.rest.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void elDocumentoOpenApiIncluyeTodosLosEndpoints() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").value(startsWith("3.")))
                .andExpect(jsonPath("$.info.title").value("CS2 Armas API"))
                .andExpect(jsonPath("$.paths['/']").exists())
                .andExpect(jsonPath("$.paths['/api/armas']").exists())
                .andExpect(jsonPath("$.paths['/api/armas/{id}']").exists())
                .andExpect(jsonPath("$.paths['/api/armas/simulacion']").exists())
                .andExpect(jsonPath("$.paths['/api/escenarios/combate-validado']").exists())
                .andExpect(jsonPath("$.paths['/api/armas/{id}/dano']").exists())
                .andExpect(jsonPath("$.paths['/api/armas/{id}/cargar-municion']").exists())
                .andExpect(jsonPath("$.paths['/api/armas/{id}/disparar']").exists())
                .andExpect(jsonPath("$.paths['/api/armas/descripcion']").exists())
                .andExpect(jsonPath("$.paths['/api/armas/equipables']").exists())
                .andExpect(jsonPath("$.paths['/api/armas/ui']").exists())
                .andExpect(jsonPath("$.paths['/api/armas/fusil']").exists())
                .andExpect(jsonPath("$.paths['/api/armas/humo']").exists())
                .andExpect(jsonPath("$.paths['/api/objetos']").exists())
                .andExpect(jsonPath("$.paths['/api/objetos/{id}']").exists())
                .andExpect(jsonPath("$.paths['/api/objetos'].get.summary").value("Listar objetos externos"))
                .andExpect(jsonPath("$.paths['/api/armas'].post.responses['201']").exists());
    }

    @Test
    void elDocumentoOpenApiIncluyeElEsquemaDeErroresDeIntegracion() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.schemas.ErrorResponse.properties.tipo").exists())
                .andExpect(jsonPath("$.paths['/api/escenarios/combate-validado'].post.responses['400'].content['*/*'].schema.$ref")
                        .value(containsString("ErrorResponse")))
                .andExpect(jsonPath("$.paths['/api/escenarios/combate-validado'].post.responses['404'].content['*/*'].schema.$ref")
                        .value(containsString("ErrorResponse")))
                .andExpect(jsonPath("$.paths['/api/escenarios/combate-validado'].post.responses['409'].content['*/*'].schema.$ref")
                        .value(containsString("ErrorResponse")))
                .andExpect(jsonPath("$.paths['/api/escenarios/combate-validado'].post.responses['502'].content['*/*'].schema.$ref")
                        .value(containsString("ErrorResponse")))
                .andExpect(jsonPath("$.paths['/api/armas'].post.responses['400'].content['*/*'].schema.$ref")
                        .value(containsString("ErrorResponse")))
                .andExpect(jsonPath("$.paths['/api/armas/simulacion'].post.responses['400'].content['*/*'].schema.$ref")
                        .value(containsString("ErrorResponse")));
    }

    @Test
    void swaggerUiEstaDisponible() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }
}
