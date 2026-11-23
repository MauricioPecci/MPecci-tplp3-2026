package py.edu.uc.lp3.rest.controller.arma;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import py.edu.uc.lp3.domain.Escopeta;
import py.edu.uc.lp3.domain.Flash;
import py.edu.uc.lp3.domain.Francotirador;
import py.edu.uc.lp3.domain.Fusil;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.domain.Humo;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.rest.dto.DanoResponse;
import py.edu.uc.lp3.rest.dto.ErrorResponse;
import py.edu.uc.lp3.rest.dto.DisparoRequest;
import py.edu.uc.lp3.rest.dto.DisparoResponse;
import py.edu.uc.lp3.rest.dto.SimulacionRequest;
import py.edu.uc.lp3.rest.dto.SimulacionResponse;
import py.edu.uc.lp3.service.ArmaService;

@Tag(name = "Armas - CRUD y simulación", description = "Alta, consulta, modificación, borrado y simulación de combate")
@RestController
@RequestMapping("/api/armas")
public class ArmaCrudController {

    private final ArmaService armaService;

    public ArmaCrudController(ArmaService armaService) {
        this.armaService = armaService;
    }

    @Operation(summary = "Listar armas", description = "Devuelve todas las armas o filtra por nombre o precio máximo.")
    @GetMapping
    public ResponseEntity<List<Arma>> getAll(
            @Parameter(description = "Fragmento del nombre a buscar") @RequestParam(required = false) String nombre,
            @Parameter(description = "Precio máximo inclusive") @RequestParam(required = false) Double precioMax) {
        return ResponseEntity.ok(armaService.findAll(nombre, precioMax));
    }

    @Operation(summary = "Obtener arma por id", description = "Devuelve un arma existente a partir de su identificador.")
    @ApiResponse(responseCode = "404", description = "No existe un arma con ese id",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<Arma> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(armaService.findById(id));
    }

    @Operation(summary = "Crear arma", description = "Persiste un arma nueva de forma polimórfica usando tipoArma; el id lo asigna la base de datos.")
    @ApiResponse(responseCode = "201", description = "Arma creada")
    @ApiResponse(responseCode = "400", description = "Los datos del arma no son válidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Arma> create(@RequestBody Arma arma) {
        Arma creado = armaService.create(arma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Crear fusil", description = "Persiste un fusil nuevo; el id lo asigna la base de datos.")
    @ApiResponse(responseCode = "201", description = "Fusil creado")
    @ApiResponse(responseCode = "400", description = "Los datos del fusil no son válidos")
    @PostMapping("/fusil")
    public ResponseEntity<Fusil> createFusil(@RequestBody Fusil arma) {
        Fusil creado = (Fusil) armaService.create(arma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Crear escopeta", description = "Persiste una escopeta nueva; el id lo asigna la base de datos.")
    @ApiResponse(responseCode = "201", description = "Escopeta creada")
    @ApiResponse(responseCode = "400", description = "Los datos de la escopeta no son válidos")
    @PostMapping("/escopeta")
    public ResponseEntity<Escopeta> createEscopeta(@RequestBody Escopeta arma) {
        Escopeta creado = (Escopeta) armaService.create(arma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Crear pistola", description = "Persiste una pistola nueva; el id lo asigna la base de datos.")
    @ApiResponse(responseCode = "201", description = "Pistola creada")
    @ApiResponse(responseCode = "400", description = "Los datos de la pistola no son válidos")
    @PostMapping("/pistola")
    public ResponseEntity<Pistola> createPistola(@RequestBody Pistola arma) {
        Pistola creado = (Pistola) armaService.create(arma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Crear francotirador", description = "Persiste un francotirador nuevo; el id lo asigna la base de datos.")
    @ApiResponse(responseCode = "201", description = "Francotirador creado")
    @ApiResponse(responseCode = "400", description = "Los datos del francotirador no son válidos")
    @PostMapping("/francotirador")
    public ResponseEntity<Francotirador> createFrancotirador(@RequestBody Francotirador arma) {
        Francotirador creado = (Francotirador) armaService.create(arma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Crear humo", description = "Persiste una granada de humo nueva; el id lo asigna la base de datos.")
    @ApiResponse(responseCode = "201", description = "Humo creado")
    @ApiResponse(responseCode = "400", description = "Los datos del humo no son válidos")
    @PostMapping("/humo")
    public ResponseEntity<Humo> createHumo(@RequestBody Humo arma) {
        Humo creado = (Humo) armaService.create(arma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Crear granada", description = "Persiste una granada nueva; el id lo asigna la base de datos.")
    @ApiResponse(responseCode = "201", description = "Granada creada")
    @ApiResponse(responseCode = "400", description = "Los datos de la granada no son válidos")
    @PostMapping("/granada")
    public ResponseEntity<Granada> createGranada(@RequestBody Granada arma) {
        Granada creado = (Granada) armaService.create(arma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Crear flash", description = "Persiste una granada flash nueva; el id lo asigna la base de datos.")
    @ApiResponse(responseCode = "201", description = "Flash creada")
    @ApiResponse(responseCode = "400", description = "Los datos de la flash no son válidos")
    @PostMapping("/flash")
    public ResponseEntity<Flash> createFlash(@RequestBody Flash arma) {
        Flash creado = (Flash) armaService.create(arma);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar arma", description = "Reemplaza los datos de un arma existente sin cambiar su tipo.")
    @ApiResponse(responseCode = "404", description = "No existe un arma con ese id")
    @ApiResponse(responseCode = "400", description = "Los datos del arma no son válidos")
    @PutMapping("/{id}")
    public ResponseEntity<Arma> update(@PathVariable Integer id, @RequestBody Arma arma) {
        return ResponseEntity.ok(armaService.update(id, arma));
    }

    @Operation(summary = "Eliminar arma", description = "Borra un arma existente por su identificador.")
    @ApiResponse(responseCode = "204", description = "Arma eliminada")
    @ApiResponse(responseCode = "404", description = "No existe un arma con ese id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        armaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Simular combate", description = "Un atacante dispara, el objetivo recibe daño y un tercer arma recarga munición.")
    @ApiResponse(responseCode = "400", description = "La simulación no cumple las reglas de dominio",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/simulacion")
    public ResponseEntity<SimulacionResponse> simular(@RequestBody SimulacionRequest request) {
        return ResponseEntity.ok(armaService.simular(request));
    }

    @Operation(summary = "Aplicar daño", description = "Reduce la durabilidad del arma indicada; si llega a 0 se destruye. El cuerpo espera la clave dano.")
    @ApiResponse(responseCode = "200", description = "Daño aplicado; la respuesta indica si el arma fue destruida")
    @ApiResponse(responseCode = "400", description = "El daño no es válido")
    @PostMapping("/{id}/dano")
    public ResponseEntity<DanoResponse> aplicarDano(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        int dano = body != null && body.get("dano") != null ? body.get("dano") : 0;
        return ResponseEntity.ok(armaService.aplicarDano(id, dano));
    }

    @Operation(summary = "Cargar munición", description = "Recarga un arma de fuego hasta su munición máxima; el cuerpo espera la clave cantidad.")
    @ApiResponse(responseCode = "400", description = "La cantidad o el tipo de arma no son válidos")
    @PostMapping("/{id}/cargar-municion")
    public ResponseEntity<Arma> cargarMunicion(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        int cantidad = body != null && body.get("cantidad") != null ? body.get("cantidad") : 0;
        return ResponseEntity.ok(armaService.cargarMunicion(id, cantidad));
    }

    @Operation(summary = "Disparar", description = "Un arma de fuego dispara a un objetivo, aplica daño, consume munición y puede destruir el objetivo.")
    @ApiResponse(responseCode = "200", description = "Disparo ejecutado")
    @ApiResponse(responseCode = "400", description = "La solicitud o el estado del arma no son válidos")
    @ApiResponse(responseCode = "404", description = "No existe el atacante u objetivo")
    @PostMapping("/{id}/disparar")
    public ResponseEntity<DisparoResponse> disparar(@PathVariable Integer id, @RequestBody DisparoRequest request) {
        return ResponseEntity.ok(armaService.disparar(id, request));
    }
}
