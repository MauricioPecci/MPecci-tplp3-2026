# Bitácora de asistencia de IA

## Asistente / agente

- **Marca:** OpenCode
- **Modelo de LLM exacto:** `big-pickle` (ID del modelo provisto por la
  herramienta: `opencode/big-pickle`)

## Resumen de los prompts

1. **"Arreglar la configuración de paquetes y hacer el código ejecutable; luego, añadir sobrecarga y sobreescritura a las clases Fusil y Humo."**
  - Configuración de paquetes/ejecutable (proyecto Cs2/, Spring Boot 4.1.1 + Maven): `chmod +x mvnw`; limpieza de `pom.xml`; renombrado de archivos con `git mv` para coincidir con clases públicas; corrección de `package` a `py.edu.uc.lp3.MPecci.Cs2.armas`. Verificado con `./mvnw package` (BUILD SUCCESS) y arranque en 8080.
  - Sobrecarga y sobreescritura: `Arma.describir()`; `Fusil` con constructor sobrecargado, `calcularDano()` (3 firmas) y `@Override describir()`; `Humo` con 3 constructores, `areaCobertura()` (2 firmas) y `@Override describir()`. Verificado con `./mvnw compile` y `jshell`.

2. **"Añadir sobrecarga y sobreescritura a las clases fusil y humo en Cs2/.../armas"** — constructores sobrecargados y sobrecarga de `calcularDano()`/`areaCobertura()`; sobrescritura de `describir()` con método concreto en `Arma`. Compilación verificada y cambio registrado en commit.

3. **"Publicá un servicio HTTP con Spring Boot sobre el modelado (CS2)..."** — reestructuración a template de paquetes (`py.edu.uc.lp3.domain`, `py.edu.uc.lp3.rest.controller`, `Application`); `Arma.describir()` abstracto y sobrescrituras en ramas/clases; `ArmaInvalidaException` e invariantes; `IndexController` (`GET /`) y `ArmaController` (construcción desde URL, JSON, 400 ante reglas rotas); pruebas de dominio/MockMvc; `README.md`, `BITACORA.md`, `docs/especificaciones-poo06-cs2.md`; commits separados.

4. **"Elegí una capacidad que compartan dos clases... declarala como interface..."** — interface `Equipable` (`ranura()`, `mensajeDeEquipado()` default); `Fusil` usa default y `Granada` (familia distinta) lo reemplaza; `GET /api/armas/equipables` usa el tipo `Equipable` y responde JSON; pruebas de dominio/MockMvc; verificación con `./mvnw test` y `curl`.

5. **"Agregar a todas las clases la característica UIcapable con imagen y sonido"** — interface `UIcapable` (`imagen()`, `sonido()`); implementada en la raíz `Arma` para que todas las clases la hereden; `GET /api/armas/ui` responde imagen/sonido en JSON; pruebas de dominio y endpoint.

6. **"Necesito un CRUD rest para la clase \"Arma\" que siga la arquitectura de 3 capas,tenga una capa de negocios con alguna validación (service) y persista en una base de datos H2 con spring data"** — configuración H2/JPA; entidad `ArmaEntity`; repositorio `ArmaRepository` (`JpaRepository`); servicio `ArmaService` con validaciones; controlador `ArmaCrudController` en `/api/armas-crud` (GET, GET/{id}, POST→201, PUT, DELETE→204). `./mvnw test` BUILD SUCCESS (20 tests).

7. **"Añade las reglas de negocio daño y carga de municion con jerarquia de excepciones del dominio y un handler por tipo"** — jerarquía `ArmaDominioException` (base) + `ArmaInvalidaException`, `DanoInvalidoException`, `CargaMunicionException`; `ArmaEntity` con `dano`, `municionActual`, `municionMaxima`; `ArmaService` con `aplicarDano`/`cargarMunicion` y endpoints `POST /{id}/dano`, `POST /{id}/cargar-municion`; `ApiExceptionHandler` con handler por tipo. `./mvnw test` BUILD SUCCESS (20 tests). Commit `1bea1a4`.

8. **"Verifica consigna POO-08... cambia las reglas de negocio... actualiza README y bitácora"** — reglas movidas al dominio (`ArmaDeFuego.reglaDeDano(...)`, `ArmaDeFuego.reglaDeCargaMunicion(...)`); `ArmaService` las dispara; `GET /` actualizado; `README.md` actualizado (H2/JPA, CRUD persistido, reglas CS2, jerarquía/handlers, decisiones POO-08); bitácora reestructurada al formato de prompts.

9. **"Saca el apartado de cambios realizados ahora de la bitacora, sigue la estructura de resumen de prompts establecida"** — se eliminó la sección `Cambios realizados ahora` y se consolidó todo en `Resumen de los prompts` (entradas 1-9).

10. **"Haz todo menos la consigna 9 (Insomnia)"** — cierre de POO-08 sin la colección Insomnia:
  - **Persistencia del dominio:** `Arma` pasó a ser `@Entity` raíz con `@Inheritance(SINGLE_TABLE)` y `@DiscriminatorColumn(name = "tipo_arma")`; `ArmaDeFuego`/`Arrojadiza` son `@MappedSuperclass`; las concretas llevan `@DiscriminatorValue`. Se eliminó `ArmaEntity` y el paquete `entity`.
  - **Jackson y JPA:** `id` autogenerado y de solo lectura; `tipoArma` con `@JsonTypeInfo`/`@JsonSubTypes`; `@Column` renombradas para evitar colisiones de campos homónimos en la tabla única; constructores con parámetros marcados con `@JsonCreator(mode = DISABLED)` para que Jackson use el constructor sin argumentos y los setters.
  - **CRUD REST:** recurso `/api/armas` (reemplaza `/api/armas-crud`) con POST→201, GET/GET{id}, PUT, DELETE→204, filtros `nombre`/`precioMax` y operaciones `POST /{id}/dano` y `POST /{id}/cargar-municion`. `ArmaNoEncontradaException` + handler 404 (`ARMA_NO_ENCONTRADA`).
  - **Simulación:** `POST /api/armas/simulacion` con DTOs `SimulacionRequest`/`SimulacionResponse`; Fusil daña a Humo, Pistola recarga munición y los tres cambios se persisten en una transacción. La respuesta informa clases, valores antes/después, daño aplicado y munición recargada.
  - **Pruebas:** `ArmaCrudControllerTest` (crear/listar/obtener/actualizar/borrar, validaciones, 404, simulación feliz y errores de daño/munición) y nuevas pruebas de dominio para `dispararA`, `recargar` y `recibirDano`; `./mvnw test` BUILD SUCCESS (34 tests).
  - **Documentación:** README actualizado (modelo persistido, tabla de endpoints, simulación, reglas, Mermaid, decisiones), nuevo `docs/especificaciones-poo08-cs2.md` y esta entrada de bitácora. Commit de código `2668068`.
  - **Exclusión:** la consigna 9 (colección Insomnia) no se implementó por pedido explícito.

11. **"https://api.restful-aoi.dev/objects necesito que agregues un servicio que consulte esta apirest con el web client de springboot"**
  - La URL indicada no resolvía (`curl: (6) Could not resolve host: api.restful-aoi.dev`); por elección del usuario se usó `https://api.restful-api.dev/objects`.
  - `pom.xml`: `spring-boot-starter-webclient`; `application.properties`: `api.restful.base-url`.
  - `ObjetoExternoService` (`WebClient.Builder`, `Flux`/`Mono`) y DTO `ObjetoExterno`; `ObjetoExternoController` con `GET /api/objetos` y `GET /api/objetos/{id}`.
  - `ApiExceptionHandler`: 404 `OBJETO_EXTERNO_NO_ENCONTRADO`, 502 `SERVICIO_EXTERNO_NO_DISPONIBLE` y 400 para `IllegalArgumentException`.
  - `ObjetoExternoServiceTest` (4 tests con `ExchangeFunction` simulado); `./mvnw test` BUILD SUCCESS (43 tests en ese momento).

12. **"Agrega OpenAPI a todos los servicios REST y swaggerUI al proyecto"**
  - Compatibilidad con Spring Boot 4.1 verificada: `springdoc-openapi-starter-webmvc-ui:3.2.0`; rutas `/v3/api-docs` y `/swagger-ui.html`.
  - `OpenApiConfig` con `@Bean OpenAPI` (título `CS2 Armas API`); `@Tag`/`@Operation`/`@Parameter`/`@ApiResponse` en `IndexController`, `ArmaController`, `ArmaCrudController` y `ObjetoExternoController`; `@Schema` en los DTOs.
  - `OpenApiDocumentationTest` (2 tests: documento OpenAPI y Swagger UI); `./mvnw test` BUILD SUCCESS (45 tests).

13. **"puedes verificar que este todo lo que añadiste? tambien actualiza el readme y la bitacora"**
  - Revisión de `git status --short` y `git diff --cached`: confirmados los cambios de WebClient/OpenAPI; los ajustes de validación en `ArmaService`, constructores `protected` y tests extra de `ArmaCrudControllerTest` eran preexistentes a estas solicitudes.
  - `./mvnw clean test` BUILD SUCCESS (45 tests, 0 errores) y lectura completa de `README.md` y `BITACORA.md`.
  - `README.md`: servicio WebClient, endpoints `/api/objetos`, OpenAPI/Swagger UI, handlers 404/502, estructura y total de tests; `BITACORA.md`: entradas 11-13.

14. **"quiero agregar log4j en nivel debug para todos los pasos de la integración con otros servicios"**
  - `pom.xml`: `spring-boot-starter-log4j2` y exclusión de `spring-boot-starter-logging` en `spring-boot-starter-data-jpa` (árbol de dependencias sin Logback).
  - `src/main/resources/log4j2.xml`: appender de consola, root `info` y nivel `debug` para `ObjetoExternoService`, `ObjetoExternoController` y `ApiExceptionHandler`.
  - Trazas `LOGGER.debug` en cada paso de la consulta a la API externa: inicio, solicitud HTTP (filtro `ExchangeFilterFunction`), status de respuesta, deserialización, conteo/finalización, errores y mapeo 404/502.
  - `./mvnw clean test` BUILD SUCCESS (45 tests); trazas DEBUG verificadas en la salida de `ObjetoExternoServiceTest`; `README.md` actualizado.

15. **"¿Qué hicimos hasta ahora?" / continuar: fix búsqueda/creación de armas, creación por subtipo y destrucción por durabilidad 0**
  - Dominio/servicio: `Arma.recibirDano(int)` clampa a la durabilidad (permite overkill), retorna daño aplicado y, si llega a 0, `ArmaService.aplicarDano`/`simular` eliminan el arma; `ArmaDeFuego.dispararA` usa el daño aplicado real.
  - Contratos REST: `POST /api/armas/{id}/dano` responde 200 con `DanoResponse {id,nombre,durabilidad,danoAplicado,destruida}`; `SimulacionResponse` incluye `objetivoDestruido`; agregados endpoints de creación por subtipo `POST /api/armas/{fusil,escopeta,pistola,francotirador,humo,granada,flash}` además del POST polimórfico.
  - Manejo de errores/config: `ApiExceptionHandler` mapea `HttpMessageNotReadableException` a 400 `CUERPO_INVALIDO` y desempaqueta causas de dominio para preservar el `tipo`; `spring.jackson.deserialization.fail-on-unknown-properties=true`.
   - Pruebas/docs: actualizados `ArmaTest` y `ArmaCrudControllerTest` (clamp/destrucción, respuesta de daño, creación por subtipo, cuerpo inconsistente→400) y documentación (`README.md`, `docs/insomnia-poo08.md`, `docs/insomnia-poo08.json`, `docs/especificaciones-poo08-cs2.md`). Verificado en verde: `ArmaTest`, `ArmaCrudControllerTest` (19), `ArmaControllerTest`, `OpenApiDocumentationTest`, `ObjetoExternoServiceTest`, `ApplicationTests` (total 50, 0 fallos).

16. **"A1/A2/A3: examinen OpenAPIs, creen un servicio que use lógica propia + regla ajena con URL externalizable y documenten con diagramas Mermaid"**
   - Acceso confirmado a A1 (`desktop-hrm8vjo...`), A2 (`ariel...`) y A3 (`100.95.213.93:8081`). El servicio local de reglas de Minecraft en `localhost:8081` no respondió, por lo que se usaron reglas de A2 y A3.
   - Reglas elegidas: A2 `POST /api/explosiones/validar` (sólo HE; *dry-run* sin detonar) y A3 `GET /armas/{tipo}/disparar` (ráfaga `1–100`, distancia no negativa). A1 ejecuta `ArmaService.simular(...)` recién después de ambas respuestas `200`.
   - Nuevo `POST /api/escenarios/combate-validado`: `EscenarioCombateController`, `EscenarioCombateService`, clientes `CompaneroA2Client`/`CompaneroA3Client` con implementaciones WebClient, DTOs `EscenarioCombateRequest/Response`, `ExplosionA2Request` y `RafagaA3Request`; excepciones `ValidacionCompaneroRechazadaException` y `CompaneroNoDisponibleException`.
   - URL externalizable en `application.properties`: `A2_BASE_URL`, `A2_EXPLOSION_VALIDAR_PATH`, `A3_BASE_URL`, `A3_RAFAGA_PATH`; trazas DEBUG agregadas en `log4j2.xml` y `ApiExceptionHandler` traduce rechazos a `400`/`404`/`409` y caídas a `502`.
   - Documentación nueva: `docs/ejercicio-poo-09-cs2.md` con un diagrama de secuencia Mermaid limpio para el camino feliz (incluye el `GET /api/armas` que muestra la jugada guardada) y diagramas separados para rechazo A2 `400`, conflicto A2 `409`, rechazo A3 `400`, compañero no disponible `502` y regla local inválida `400`. Los diagramas de rechazo A2/A3 muestran el `GET` previo/posterior con el H2 igual y el cuerpo con `companero` + `RECHAZO_A2`/`RECHAZO_A3`; `README.md` referencia el caso.
   - Pruebas nuevas: `CompaneroA2WebClientServiceTest`, `CompaneroA3WebClientServiceTest`, `EscenarioCombateServiceTest`, `ApiExceptionHandlerCompaneroTest` y aserción en `OpenApiDocumentationTest`. Verificado: 12 pruebas puntuales en verde. `./mvnw test`: 62 pruebas, 2 fallos preexistentes de `ArmaControllerTest` (esperan 400 y obtienen 200), sin errores nuevos.

17. **"Deja en `insomnia/` o `docs/insomnia/` la colección de APIs cruzadas, y en el README el mapa del grupo; agrega log4j en nivel debug para todos los pasos de la integración con otros servicios"**
   - Colección nueva: `docs/insomnia/insomnia-poo09.json` con formato Insomnia v4, environment **Grupo CS2** (`a1BaseUrl`, `a2BaseUrl`, `a3BaseUrl`, ids locales y `granadaId`), OpenAPI de A1/A2/A3, validaciones directas de A2/A3, setup de la H2 de A1, escenario feliz, rechazo A2, rechazo A3 y `GET` previo/posterior para demostrar que la base queda igual. Incluye respuestas guardadas del feliz y de cada rechazo.
   - `README.md`: tabla del mapa A1/A2/A3 con dueño, repositorio/rama, URL base, OpenAPI y dos reglas por dueño; orden de clonado/arranque de los tres servicios y enlace a la colección POO-09.
   - Trazas DEBUG: `EscenarioCombateController` registra recepción y respuesta; `EscenarioCombateService` registra validación de entrada, los tres pasos (A2 → A3 → simulación local), respuestas y rechazos/no disponibilidad/regla local; `CompaneroA2WebClientService` y `CompaneroA3WebClientService` registran configuración, validación local, URL preparada, status, cuerpo de respuesta y errores. `log4j2.xml` habilita el logger del controller del escenario; la integración con `api.restful-api.dev` ya tenía trazas DEBUG.
   - Verificación: JSON válido con 38 recursos y sin `_id` duplicados. La suite final quedó en 63 pruebas en verde tras las entradas 18 y 19.

18. **Cierre de las validaciones que faltaban y de la navegación**
   - `ArmaService.validar` quedó público para que los endpoints de demostración usen las mismas reglas del servicio: `ArmaController` valida el `Fusil` y el `Humo` construidos desde la URL, de modo que `precio=-1` y `precision=250` devuelven `400` con `$.error`.
   - Regla de escopeta endurecida: `perdigon <= 0` ahora se rechaza con `ARMA_INVALIDA` y no persiste. Agregado el caso `perdigon: 0` a `ArmaCrudControllerTest`; también se mantiene el rechazo de `dano` negativo.
   - `docs/ejercicio-poo-09-cs2.md`: la navegación ahora apunta al enunciado oficial `ejercicio-poo-09-apis-cruzadas-2026-11-16.md` y conserva el enlace al ejemplo UNO.
   - Verificado: `./mvnw -Dtest=ArmaControllerTest,ArmaCrudControllerTest test` → 32 pruebas en verde.

19. **Esquema de errores en OpenAPI**
   - DTO nuevo `ErrorResponse` con `tipo`, `error`, `rechazo`, `companero`, `codigoExterno` y `cuerpoExterno`.
   - `EscenarioCombateController` documenta los bodies de error `400`, `404`, `409` y `502` con `$ref` a `ErrorResponse`; `ArmaCrudController` documenta los errores de `POST /api/armas`, `GET /api/armas/{id}` y `POST /api/armas/simulacion`.
   - `OpenApiDocumentationTest` verifica el esquema en `components.schemas.ErrorResponse` y las referencias desde los response codes de integración y CRUD.
   - Verificación final: `git diff --check` sin hallazgos; `./mvnw test` → 63 pruebas, 0 fallos y 0 errores.

20. **Limpieza de diagramas Mermaid**
   - `docs/ejercicio-poo-09-cs2.md` quedó con un solo bloque Mermaid bajo `## Diagrama limpio`: cliente → A2 → A3 → simulación local en A1 → `201` → `GET /api/armas`.
   - Eliminados los bloques y secciones de rechazo A2 `400`/`409`, rechazo A3 `400`, compañero no disponible `502`, regla local inválida `400` y la tabla de respuestas de error. El paso 3 ya no menciona códigos de error.
   - `README.md` referencia el archivo como diagrama Mermaid del recorrido exitoso.

21. **Unificación de las colecciones Insomnia**
   - Reemplazados `docs/insomnia-poo08.json` y `docs/insomnia/insomnia-poo09.json` por un único export `docs/insomnia/insomnia-cs2.json` con formato Insomnia v4.
   - El archivo único conserva dos workspaces (`POO-08 CS2` y `POO-09 CS2 - APIs cruzadas`), 4 environments, 10 carpetas, 44 requests y 11 respuestas guardadas: 71 recursos con ids únicos y `parentId` válidos.
   - Actualizadas las referencias en `README.md`, `docs/insomnia-poo08.md` y `docs/especificaciones-poo08-cs2.md`.
