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