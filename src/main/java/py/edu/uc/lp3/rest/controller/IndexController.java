package py.edu.uc.lp3.rest.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public Map<String, Object> index() {
        return Map.of(
                "servicio", "cs2-armas-api",
                "estado", "activo",
                "endpoints", List.of(
                        "GET /",
                        "GET /api/armas/descripcion",
                        "GET /api/armas/fusil?nombre=AK-47&distancia=100&critico=true",
                        "GET /api/armas/humo?nombre=Humo de cortina&radio=6&radioExtra=2"));
    }
}
