package py.edu.uc.lp3.rest.controller.arma;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.rest.dto.SimulacionRequest;
import py.edu.uc.lp3.rest.dto.SimulacionResponse;
import py.edu.uc.lp3.service.ArmaService;

@RestController
@RequestMapping("/api/armas")
public class ArmaCrudController {

    private final ArmaService armaService;

    public ArmaCrudController(ArmaService armaService) {
        this.armaService = armaService;
    }

    @GetMapping
    public ResponseEntity<List<Arma>> getAll(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Double precioMax) {
        return ResponseEntity.ok(armaService.findAll(nombre, precioMax));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Arma> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(armaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Arma> create(@RequestBody Arma arma) {
        Arma creado = armaService.create(arma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Arma> update(@PathVariable Integer id, @RequestBody Arma arma) {
        return ResponseEntity.ok(armaService.update(id, arma));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        armaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/simulacion")
    public ResponseEntity<SimulacionResponse> simular(@RequestBody SimulacionRequest request) {
        return ResponseEntity.ok(armaService.simular(request));
    }

    @PostMapping("/{id}/dano")
    public ResponseEntity<Arma> aplicarDano(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        int dano = body != null && body.get("dano") != null ? body.get("dano") : 0;
        return ResponseEntity.ok(armaService.aplicarDano(id, dano));
    }

    @PostMapping("/{id}/cargar-municion")
    public ResponseEntity<Arma> cargarMunicion(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        int cantidad = body != null && body.get("cantidad") != null ? body.get("cantidad") : 0;
        return ResponseEntity.ok(armaService.cargarMunicion(id, cantidad));
    }
}
