# Ejercicio POO-08 — CRUD REST, H2 y simulación sobre Counter-Strike 2

**Alumno:** Mauricio Pecci  
**Repositorio:** https://github.com/MauricioPecci/MPecci-tplp3-2026  
**Dominio elegido:** Counter-Strike 2 (CS2)  
**Enunciado:** [ejercicio-poo-08-crud-h2-insomnia-2026-10-28.md](https://github.com/alefq/afq-taller-git-2024/blob/main/docs/ejercicio-poo-08-crud-h2-insomnia-2026-10-28.md)  
**Rúbrica:** [RUBRICA-ejercicios-lp3-2026.md](https://github.com/alefq/afq-taller-git-2024/blob/main/docs/RUBRICA-ejercicios-lp3-2026.md)

## 1. Objetivo

Completar el servicio HTTP de CS2 con un **CRUD REST persistido en H2** para la clase `Arma`, manteniendo la arquitectura de tres capas (controller → service → repository), las validaciones de negocio y la jerarquía de excepciones. Además, se agrega una **simulación REST** que involucra tres clases concretas y deja el resultado visible en un `GET` posterior.

## 2. Consignas aplicadas

1. **CRUD REST de `Arma` con arquitectura de tres capas.** `ArmaCrudController` recibe HTTP, `ArmaService` aplica validaciones/reglas y `ArmaRepository` (Spring Data) persiste. El controller no guarda listas en memoria ni asigna estado inválido.

2. **Persistencia en H2 con Spring Data JPA.** `application.properties` configura `jdbc:h2:mem:armasdb`, consola `/h2-console`, `ddl-auto=update` y SQL visible. `ArmaRepository extends JpaRepository<Arma, Integer>` con consultas derivadas (`findByNombreContainingIgnoreCase`, `findByPrecioLessThanEqual`, `findByDurabilidadLessThan`).

3. **La jerarquía de dominio es la jerarquía JPA.** No existe una entidad paralela:
   - `Arma`: `@Entity`, `@Table(name = "armas")`, `@Inheritance(SINGLE_TABLE)` y `@DiscriminatorColumn(name = "tipo_arma")`.
   - `ArmaDeFuego` y `Arrojadiza`: `@MappedSuperclass`.
   - `Fusil`, `Escopeta`, `Pistola`, `Francotirador`, `Humo`, `Granada`, `Flash`: `@Entity` + `@DiscriminatorValue`.
   - El `id` es `@Id @GeneratedValue(strategy = IDENTITY)` y `@JsonProperty(access = READ_ONLY)`.
   - Jackson distingue el subtipo con `tipoArma` (`@JsonTypeInfo` / `@JsonSubTypes`); las columnas homónimas entre subtipos se renombran con `@Column` para evitar colisiones en `SINGLE_TABLE`.

4. **Validaciones de negocio.** Se validan nombre no vacío, precio no negativo, durabilidad no negativa, precisión 0–100, daño no negativo, munición actual/máxima coherente y radio/distancia/duración positivos. Las invariantes viven en el dominio y el service valida el estado antes de persistir.

5. **CRUD completo con códigos HTTP.** `POST /api/armas` → 201; `GET /api/armas` y `GET /api/armas/{id}` → 200; `PUT /api/armas/{id}` → 200; `DELETE /api/armas/{id}` → 204. Un id inexistente responde 404 con `tipo: ARMA_NO_ENCONTRADA`. El listado acepta filtros `nombre` y `precioMax`.

6. **Reglas de daño y munición con excepciones y handlers.** El dominio implementa `Arma.recibirDano(int)`, `ArmaDeFuego.dispararA(Arma, int, boolean)` y `ArmaDeFuego.recargar(int)`. Las excepciones (`ArmaInvalidaException`, `DanoInvalidoException`, `CargaMunicionException`, `ArmaNoEncontradaException`) heredan de `ArmaDominioException` y `ApiExceptionHandler` responde 400/404 con un `tipo` distinto por caso.

7. **Simulación REST que involucra tres clases y persiste.** `POST /api/armas/simulacion` recibe `atacanteId`, `objetivoId`, `recargadorId`, `distancia`, `critico` y `municion`. Un **Fusil** dispara y daña a un **Humo**; una **Pistola** recarga munición. Los tres cambios se guardan con JPA y se verifican con `GET` posteriores. La operación es transaccional: si una regla falla, no queda estado parcial.

8. **Documentación y diagrama.** `README.md` describe el modelo persistido, los endpoints, las reglas, la simulación y las decisiones; responde las preguntas de «Qué hay que pensar» y incluye un diagrama Mermaid alineado con `src/`.

9. **Colección Insomnia (consigna 9).** Incluida como colección exportable en [`docs/insomnia-poo08.json`](insomnia-poo08.json), con guía en [`docs/insomnia-poo08.md`](insomnia-poo08.md). Contiene CRUD, filtros, endpoints de interfaces, simulación y un request por cada excepción hija, con environment `Local` y requests encadenados por variables (`fusilId`, `humoId`, `pistolaId`, `tempId`).

## 3. Cómo probarlo

Requisitos: Java 21 y Maven (el wrapper `./mvnw` resuelve Maven).

```bash
git clone git@github.com:MauricioPecci/MPecci-tplp3-2026.git
cd MPecci-tplp3-2026
./mvnw test          # 34 pruebas: dominio + CRUD/simulación + endpoints POO-06/07
./mvnw spring-boot:run
```

Verificar con curl (en otra terminal):

```bash
# 1. Crear Fusil (id 1), Humo (id 2) y Pistola (id 3)
curl -X POST "http://localhost:8080/api/armas" -H "Content-Type: application/json" \
  -d '{"tipoArma":"FUSIL","nombre":"AK-47","precio":2700,"durabilidad":100,"dano":36,"precision":71,"recarga":2.4,"velocidad":600,"municionActual":30,"municionMaxima":30,"automatica":true,"mira":2,"retroceso":3,"silenciador":false}'

curl -X POST "http://localhost:8080/api/armas" -H "Content-Type: application/json" \
  -d '{"tipoArma":"HUMO","nombre":"Humo de cortina","precio":300,"durabilidad":100,"tipo":"humo","radio":6.0,"distancia":25.0,"duracion":18.0}'

curl -X POST "http://localhost:8080/api/armas" -H "Content-Type: application/json" \
  -d '{"tipoArma":"PISTOLA","nombre":"USP-S","precio":200,"durabilidad":100,"dano":35,"precision":70,"recarga":1.5,"velocidad":352,"municionActual":12,"municionMaxima":20,"automatica":false,"silenciador":true}'

# 2. Listar y filtrar
curl "http://localhost:8080/api/armas"
curl "http://localhost:8080/api/armas?nombre=AK"
curl "http://localhost:8080/api/armas?precioMax=500"

# 3. Simulación: Fusil daña al Humo y Pistola recarga
curl -X POST "http://localhost:8080/api/armas/simulacion" -H "Content-Type: application/json" \
  -d '{"atacanteId":1,"objetivoId":2,"recargadorId":3,"distancia":0,"critico":false,"municion":5}'

# 4. Verificar la persistencia del resultado
curl "http://localhost:8080/api/armas/1"   # municionActual = 29
curl "http://localhost:8080/api/armas/2"   # durabilidad = 64
curl "http://localhost:8080/api/armas/3"   # municionActual = 17

# 5. Operaciones individuales y errores
curl -X POST "http://localhost:8080/api/armas/1/dano" -H "Content-Type: application/json" -d '{"dano":40}'
curl -X POST "http://localhost:8080/api/armas/3/cargar-municion" -H "Content-Type: application/json" -d '{"cantidad":5}'
curl -w "%{http_code}\n" "http://localhost:8080/api/armas/999999"   # 404 ARMA_NO_ENCONTRADA
```

Respuesta esperada de la simulación (extracto):

```json
{
  "atacanteClase": "Fusil",
  "atacanteMunicionAntes": 30,
  "atacanteMunicionDespues": 29,
  "objetivoClase": "Humo",
  "objetivoDurabilidadAntes": 100,
  "objetivoDurabilidadDespues": 64,
  "recargadorClase": "Pistola",
  "recargadorMunicionAntes": 12,
  "recargadorMunicionDespues": 17,
  "danoAplicado": 36,
  "municionRecargada": 5
}
```

Alternativa con Insomnia: importar `docs/insomnia-poo08.json`, activar el environment `Local` y seguir `docs/insomnia-poo08.md`.

## 4. Enlace al commit de la solución

Commit de código: <https://github.com/MauricioPecci/MPecci-tplp3-2026/commit/266806889448df36305a78a309e449f1e61cca55>

La colección Insomnia y su guía están en `docs/insomnia-poo08.json` y `docs/insomnia-poo08.md`.

## 5. Recorrido de corrección (5 a 7 minutos)

1. Abrir el enlace al commit y revisar el historial: CRUD/entidades/simulación y documentación.
2. Leer el README: modelo persistido (`SINGLE_TABLE` + `tipo_arma`), tabla de endpoints, simulación y decisiones.
3. Ver `Arma` como `@Entity` raíz, `ArmaDeFuego`/`Arrojadiza` como `@MappedSuperclass` y las concretas con `@DiscriminatorValue`; confirmar que no queda `ArmaEntity`.
4. Arrancar `./mvnw spring-boot:run` y correr la secuencia curl: crear las tres armas, ejecutar la simulación y verificar con `GET` que la munición del Fusil, la durabilidad del Humo y la munición de la Pistola cambiaron y persistieron.
5. Importar `docs/insomnia-poo08.json` en Insomnia, activar el environment `Local` y ejecutar las carpetas en orden; `docs/insomnia-poo08.md` detalla los resultados esperados.
6. Pregunta de anclaje: el controller solo traduce HTTP; el service valida y orquesta; el dominio aplica las reglas y lanza excepciones; el repositorio persiste. Un `POST /api/armas/{id}/dano` con daño mayor a la durabilidad o una recarga que supera el máximo responde 400 y no deja estado parcial.
