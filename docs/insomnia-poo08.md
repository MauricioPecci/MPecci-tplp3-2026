# Guía de la colección Insomnia — POO-08 CS2

## Archivos

- **Colección exportable:** [`docs/insomnia-poo08.json`](insomnia-poo08.json)
- **Guía:** este archivo

La colección usa el formato de exportación **Insomnia v4** y contiene **24 requests** agrupados en cinco carpetas:

| Carpeta | Contenido |
|---|---|
| `00 - Setup` | Crea el Fusil, el Humo y la Pistola de la simulación. |
| `01 - CRUD` | Listado, filtros, obtención, actualización, borrado y verificación de 404. |
| `02 - Interfaces` | Demostración de `Equipable` y `UIcapable`. |
| `03 - Simulación` | Ejecuta la simulación y verifica el estado persistido de las tres armas. |
| `04 - Excepciones` | Un request por cada excepción hija del dominio. |

## Requisitos

1. Java 21 y el wrapper de Maven.
2. Insomnia instalado.
3. La aplicación corriendo en `http://localhost:8080`:

```bash
./mvnw spring-boot:run
```

La base es H2 en memoria (`jdbc:h2:mem:armasdb`). Al reiniciar la aplicación se borran los datos y los ids vuelven a empezar en 1.

## Importar la colección

1. Abrir Insomnia.
2. Menú de la aplicación → **Import/Export** → **Import Data** → **From File**.
3. Seleccionar `docs/insomnia-poo08.json`.
4. Importar como colección. Debe aparecer **POO-08 CS2**.
5. Abrir el selector de environment (arriba a la izquierda) y activar **Local**.

## Environment `Local`

La colección trae estos valores:

| Variable | Valor por defecto | Uso |
|---|---:|---|
| `baseUrl` | `http://localhost:8080` | URL base de la API. |
| `fusilId` | `1` | Fusil AK-47 creado en `00 - Setup`. |
| `humoId` | `2` | Humo creado en `00 - Setup`. |
| `pistolaId` | `3` | Pistola USP-S creada en `00 - Setup`. |
| `tempId` | `4` | Fusil temporal creado en `01 - CRUD`. |

Los valores por defecto funcionan si la base está **limpia** y se ejecuta la colección en orden. Si la base ya tiene datos, reemplazar los ids por los que devuelva cada `POST`.

## Orden de ejecución

### 00 - Setup

Ejecutar en orden. En una base limpia los ids son 1, 2 y 3.

| Request | Método | Resultado esperado |
|---|---|---|
| `01 Crear Fusil` | `POST /api/armas` | `201`; `id = 1`; `tipoArma = FUSIL`. |
| `02 Crear Humo` | `POST /api/armas` | `201`; `id = 2`; `tipoArma = HUMO`. |
| `03 Crear Pistola` | `POST /api/armas` | `201`; `id = 3`; `tipoArma = PISTOLA`. |

Si los ids devueltos no son 1, 2 y 3, copiarlos en `fusilId`, `humoId` y `pistolaId` del environment.

### 01 - CRUD

| Request | Método | Resultado esperado |
|---|---|---|
| `01 Listar todas` | `GET /api/armas` | `200`; tres armas. |
| `02 Filtrar por nombre` | `GET /api/armas?nombre=AK` | `200`; solo el AK-47. |
| `03 Filtrar por precio máximo` | `GET /api/armas?precioMax=500` | `200`; Humo y Pistola. |
| `04 Obtener Fusil por id` | `GET /api/armas/{{ fusilId }}` | `200`; el Fusil. |
| `05 Crear Fusil temporal` | `POST /api/armas` | `201`; `id = 4` en base limpia. |
| `06 Actualizar Fusil temporal` | `PUT /api/armas/{{ tempId }}` | `200`; nombre `Fusil temporal actualizado`. |
| `07 Obtener Fusil temporal` | `GET /api/armas/{{ tempId }}` | `200`; datos actualizados. |
| `08 Borrar Fusil temporal` | `DELETE /api/armas/{{ tempId }}` | `204`; sin cuerpo. |
| `09 Verificar borrado (404)` | `GET /api/armas/{{ tempId }}` | `404`; `tipo = ARMA_NO_ENCONTRADA`. |

Después de `05 Crear Fusil temporal`, copiar el `id` de la respuesta en `tempId` si no es `4`.

### 02 - Interfaces

| Request | Método | Resultado esperado |
|---|---|---|
| `01 Interface Equipable` | `GET /api/armas/equipables` | `200`; lista con `Fusil` y `Granada`, `tipoDeInterfaz = Equipable`. |
| `02 Interface UIcapable` | `GET /api/armas/ui` | `200`; lista con las clases y sus datos de imagen/sonido. |

Estos endpoints son demostraciones de las interfaces y no dependen de los datos persistidos.

### 03 - Simulación

Ejecutar con el Fusil, el Humo y la Pistola recién creados.

| Request | Método | Resultado esperado |
|---|---|---|
| `01 Ejecutar simulación` | `POST /api/armas/simulacion` | `200`; Fusil 30→29, Humo 100→64, Pistola 12→17. |
| `02 Verificar Fusil` | `GET /api/armas/{{ fusilId }}` | `200`; `municionActual = 29`. |
| `03 Verificar Humo` | `GET /api/armas/{{ humoId }}` | `200`; `durabilidad = 64`. |
| `04 Verificar Pistola` | `GET /api/armas/{{ pistolaId }}` | `200`; `municionActual = 17`. |

La simulación es transaccional: si falla una regla, no deja estado parcial. Ejecutarla dos veces sin reiniciar la aplicación cambia los valores de partida y puede fallar por munición o durabilidad.

### 04 - Excepciones

Estos requests están diseñados para fallar; el resultado esperado es un error controlado.

| Request | Método | Resultado esperado |
|---|---|---|
| `01 POST ilegal` | `POST /api/armas` con precio negativo | `400`; `tipo = ARMA_INVALIDA`. |
| `02 ID inexistente` | `GET /api/armas/999999` | `404`; `tipo = ARMA_NO_ENCONTRADA`. |
| `03 Daño inválido` | `POST /api/armas/{{ fusilId }}/dano` con `dano: -1` | `400`; `tipo = DANO_INVALIDO`. |
| `04 Recarga inválida` | `POST /api/armas/{{ pistolaId }}/cargar-municion` con `cantidad: 999` | `400`; `tipo = CARGA_MUNICION_INVALIDA`. |
| `05 Recargar Humo` | `POST /api/armas/{{ humoId }}/cargar-municion` | `400`; `tipo = ARMA_INVALIDA` (el Humo no es arma de fuego). |
| `06 Operación con id inexistente` | `POST /api/armas/999999/dano` | `404`; `tipo = ARMA_NO_ENCONTRADA`. |

## Notas y solución de problemas

- **Connection refused:** la aplicación no está corriendo; ejecutar `./mvnw spring-boot:run`.
- **404 inesperado en CRUD o simulación:** los ids del environment no coinciden con los datos actuales. Reiniciar la aplicación y volver a ejecutar `00 - Setup`, o actualizar los ids.
- **La simulación falla en la segunda ejecución:** es esperado si no se reinició la base; el Humo ya recibió daño y la Pistola ya tiene más munición.
- **`tempId` incorrecto:** se puede dejar el valor por defecto `4` solo si la base está limpia. Si no, copiar el `id` devuelto por `05 Crear Fusil temporal`.
- **H2 en memoria:** no hay persistencia entre reinicios. Para una corrida reproducible, reiniciar la aplicación antes de importar/ejecutar la colección.
- **Consola H2:** `http://localhost:8080/h2-console`, JDBC URL `jdbc:h2:mem:armasdb`, usuario `sa`, contraseña vacía.
