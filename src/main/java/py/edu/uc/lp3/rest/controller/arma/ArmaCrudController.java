package py.edu.uc.lp3.rest.controller.arma;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.entity.ArmaEntity;
import py.edu.uc.lp3.service.ArmaService;

@RestController
@RequestMapping("/api/armas-crud")
public class ArmaCrudController {

    private final ArmaService armaService;

    public ArmaCrudController(ArmaService armaService) {
        this.armaService = armaService;
    }

    @GetMapping
    public ResponseEntity<List<ArmaEntity>> getAll() {
        return ResponseEntity.ok(armaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArmaEntity> getById(@PathVariable Long id) {
        return armaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArmaEntity> create(@RequestBody ArmaEntity arma) {
        ArmaEntity creado = armaService.create(arma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArmaEntity> update(@PathVariable Long id, @RequestBody ArmaEntity arma) {
        ArmaEntity actualizado = armaService.update(id, arma);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        armaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
