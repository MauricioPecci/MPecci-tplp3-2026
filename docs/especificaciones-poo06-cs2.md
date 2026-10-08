# Ejercicio POO-06 — Especificaciones aplicadas al dominio Counter-Strike 2

**Alumno:** Mauricio Pecci
**Repositorio:** https://github.com/MauricioPecci/MPecci-tplp3-2026
**Dominio elegido:** Counter-Strike 2 (CS2)
**Enunciado:** [ejercicio-poo-06-revision-paquetes-constructores-2026-09-30.md](https://github.com/alefq/afq-taller-git-2024/blob/main/docs/ejercicio-poo-06-revision-paquetes-constructores-2026-09-30.md)
**Rúbrica:** [RUBRICA-ejercicios-lp3-2026.md](https://github.com/alefq/afq-taller-git-2024/blob/main/docs/RUBRICA-ejercicios-lp3-2026.md)

## 1. Objetivo

Publicar un servicio HTTP con Spring Boot sobre el modelado de Counter-Strike 2:
un inventario de armas con herencia ("es un"), sobreescritura, ocultamiento de
la información, paquetes con sentido, constructores simples y sobrecargados y
sobrecarga de mensajes del dominio. Al clonar, `./mvnw spring-boot:run` levanta
el servicio y se lo usa desde la web, sin abrir un `main()` en el IDE.

## 2. Consignas aplicadas al dominio

1. **Paquetes del template.** Las clases viven en
   `src/main/java/py/edu/uc/lp3/`: el dominio en `domain`, los servicios REST en
   `rest.controller`, la excepción de dominio en `exceptions` y
   `Application.java` solo arranca Spring Boot.

2. **Método abstracto y dos clases hijas.** `Arma.describir()` es abstracto
   (`public abstract String describir()`). Dos ramas independientes lo
   implementan —`ArmaDeFuego` y `Arrojadiza`— y las clases concretas `Fusil`,
   `Humo`, `Escopeta`, `Pistola`, `Francotirador`, `Granada` y `Flash` lo
   **sobrescriben** con su propia implementación (misma firma, `@Override`).

3. **Sobreescritura observable en JSON.** `ArmaController` declara `Arma fusil =
   new Fusil(...)` y `Arma humo = new Humo(...)`, habla con ambas a través del
   tipo padre y responde el texto de `describir()` sobreescrito (endpoint
   `/api/armas/descripcion`).

4. **Constructores simples y sobrecargados.** La instancia se construye desde la
   URL: si vienen mira/retroceso/silenciador se usa el constructor sobrecargado
   del `Fusil`; si no, el simple. En `Humo` se elige entre el simple, el que
   lleva `radio` o el completo. Cada firma llama a `super(...)` y deja el objeto
   en un estado legal.

5. **Sobrecarga de un mensaje del dominio.** `Fusil.calcularDano()`,
   `calcularDano(int distancia)` y `calcularDano(int distancia, boolean
   critico)`; `Humo.areaCobertura()` y `areaCobertura(double radioExtra)`.
   Disparar sin más datos o indicando distancia/crítico, por ejemplo.

6. **Reglas que rechazan.** Precio negativo, `id <= 0`, precisión fuera de 0–100,
   radio/distancia/duración no positivos y nombre vacío se rechazan en el
   dominio con `ArmaInvalidaException`; el controller informa el resultado con
   HTTP `400` y el motivo en JSON. El controller no puede asignar el daño ni el
   precio a mano: son privados y los setters validan.

7. **Dos servicios REST.** `GET /` (`IndexController`) confirma que el servicio
   está vivo y `ArmaController` construye desde la URL y devuelve JSON.

## 3. Cómo probarlo

Requisitos: Java 21 y Maven (el wrapper `./mvnw` resuelve Maven).

```bash
git clone git@github.com:MauricioPecci/MPecci-tplp3-2026.git
cd MPecci-tplp3-2026
./mvnw test          # 16 pruebas: dominio + endpoints
./mvnw spring-boot:run
```

Verificar en el navegador o con curl:

```bash
# 1. Servicio vivo
curl http://localhost:8080/

# 2. Mensaje abstracto sobreescrito por las dos clases hijas (JSON)
curl http://localhost:8080/api/armas/descripcion

# 3. Fusil construido desde la URL con sobrecarga de calcularDano
curl "http://localhost:8080/api/armas/fusil?nombre=AK-47&mira=3&distancia=100&critico=true"

# 4. Humo construido con un constructor sobrecargado y areaCobertura() sobrecargado
curl "http://localhost:8080/api/armas/humo?radio=10&radioExtra=2"

# 5. Regla del dominio rota → la clase rechaza y el controller responde 400
curl -w "%{http_code}\n" "http://localhost:8080/api/armas/fusil?precio=-1"
```

Respuesta esperada del punto 5:

```json
{"rechazo":"el dominio no permite ese estado","error":"El precio no puede ser negativo: -1.0"} [400]
```

## 4. Enlace al commit de la solución

<https://github.com/MauricioPecci/MPecci-tplp3-2026/commit/d3f92d1ca1691087a2f1549638f32a6262a2d63e>

## 5. Recorrido de corrección (5 a 7 minutos)

1. Abrir el enlace al commit y ver en el historial los commits separados
   (reestructuración, dominio, REST, pruebas, documentación).
2. Leer el README: licencia Apache 2.0, diagrama Mermaid alineado con `src/` y
   el apartado de sobrecarga y sobreescritura.
3. Ver `Arma.describir()` abstracto y su sobrescritura en `Fusil` y `Humo`;
   constructores simples y sobrecargados que llaman a `super`.
4. Arrancar `./mvnw spring-boot:run` y correr los cinco puntos de la sección 3.
5. Pregunta de anclaje: el controller solo recibe valores por URL y llama
   mensajes; los atributos son privados y la clase valida, por lo que no queda un
   estado imposible.