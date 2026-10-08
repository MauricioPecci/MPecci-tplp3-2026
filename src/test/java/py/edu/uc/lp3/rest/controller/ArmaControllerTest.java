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
class ArmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void indexConfirmaQueElServicioEstaVivo() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("activo"));
    }

    @Test
    void elMensajeAbstractoSePideALasDosClasesHijasATravesDelTipoPadre() throws Exception {
        mockMvc.perform(get("/api/armas/descripcion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clase").value("Fusil"))
                .andExpect(jsonPath("$[0].heredaDe").value("Arma"))
                .andExpect(jsonPath("$[0].descripcion").value(startsWith("Fusil[")))
                .andExpect(jsonPath("$[1].clase").value("Humo"))
                .andExpect(jsonPath("$[1].descripcion").value(startsWith("Humo[")));
    }

    @Test
    void elFusilSeConstruyeDesdeLaUrlYSobrecargaCalcularDano() throws Exception {
        mockMvc.perform(get("/api/armas/fusil")
                        .param("nombre", "AK-47")
                        .param("distancia", "100")
                        .param("critico", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clase").value("Fusil"))
                .andExpect(jsonPath("$.dano").value(52))
                .andExpect(jsonPath("$.descripcion").value(containsString("silenciador=false")));
    }

    @Test
    void elHumoSeConstruyeDesdeLaUrlConUnConstructorSobrecargado() throws Exception {
        mockMvc.perform(get("/api/armas/humo").param("radio", "10.0").param("radioExtra", "2.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clase").value("Humo"))
                .andExpect(jsonPath("$.descripcion").value(containsString("radio=10.0")))
                .andExpect(jsonPath("$.areaCobertura").value(org.hamcrest.Matchers.closeTo(Math.PI * 144.0, 0.01)));
    }

    @Test
    void unaReglaDelDominioRompidaSeRechazaCon400() throws Exception {
        mockMvc.perform(get("/api/armas/fusil").param("precio", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("precio")));
    }

    @Test
    void unaPrecisionFueraDeRangoSeRechazaCon400() throws Exception {
        mockMvc.perform(get("/api/armas/fusil").param("precision", "250"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("precisión")));
    }
}
