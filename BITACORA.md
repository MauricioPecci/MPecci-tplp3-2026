# Bitácora de asistencia de IA

## Asistente / agente

- **Marca:** OpenCode
- **Modelo de LLM exacto:** `big-pickle` (ID del modelo provisto por la
  herramienta: `opencode/big-pickle`)

## Resumen de los prompts

1. **"Añadir sobrecarga y sobreescritura a las clases fusil y humo en
   Cs2/.../armas"** — se sumaron constructores sobrecargados y la sobrecarga de
   `calcularDano()` en `Fusil` y `areaCobertura()` en `Humo`; la sobrescritura de
   `describir()` en ambas clases, con el método concreto en la base `Arma`.
   Se verificó la compilación y se registró el cambio en un commit.

2. **"Publicá un servicio HTTP con Spring Boot sobre el modelado (CS2). Las
   clases siguen el template de paquetes. Se construye con constructores simples
   y sobrecargados. Sobrecarga y sobreescritura, un método abstracto con dos
   clases hijas, y dos servicios REST: GET / y un controller que construye desde
   la URL y responde JSON"** — incluyó:
   - reestructurar el proyecto a la raíz y mover las clases a
     `py.edu.uc.lp3.domain` y los servicios a `py.edu.uc.lp3.rest.controller`
     (template de paquetes), con `py.edu.uc.lp3.Application` para arrancar;
   - volver abstracto `Arma.describir()` y sobrescribirlo en las dos ramas
     independientes (`ArmaDeFuego`/`Arrojadiza`) y en las clases concretas
     `Fusil`, `Humo`, `Escopeta`, `Pistola`, `Francotirador`, `Granada` y
     `Flash`;
   - agregar `ArmaInvalidaException` y validación de invariantes en los
     constructores y setters del dominio;
   - implementar `IndexController` (`GET /`) y `ArmaController` que construye
     instancias desde los parámetros de la URL (elección de constructor simple o
     sobrecargado) responde JSON y rechaza 400 ante reglas rotas;
   - escribir pruebas de dominio y de endpoints MockMvc, verificar el arranque
     con `./mvnw spring-boot:run` y probar los endpoints con `curl`;
   - generar `README.md` con licencia Apache 2.0, diagrama Mermaid y el apartado
     de sobrecarga/sobreescritura, `BITACORA.md` y
     `docs/especificaciones-poo06-cs2.md`;
   - publicar con commits separados y el enlace al commit de la solución.