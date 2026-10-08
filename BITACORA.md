# Bitácora de asistencia de IA

## Asistente / agente

- **Marca:** OpenCode
- **Modelo de LLM exacto:** `big-pickle` (ID del modelo provisto por la
  herramienta: `opencode/big-pickle`)

## Resumen de los prompts

1. **"Arreglar la configuración de paquetes y hacer el código ejecutable; luego, añadir sobrecarga y sobreescritura a las clases Fusil y Humo."**
  -Configuración de paquetes / ejecutable (proyecto Cs2/, Spring Boot 4.1.1 + Maven):
  -chmod +x mvnw (no tenía permiso de ejecución).
  -pom.xml: eliminados elementos vacíos inválidos (<licenses>, <developers>, <scm>, <url/>) y añadidos <name>/<description>.
  -Renombrados 10 archivos de armas/ con git mv para coincidir con la clase pública (p. ej. ArmadeFuegoCs2.java → ArmaDeFuego.java, ArmasCs2.java → Arma.java).
  -Corregido package armas; → package py.edu.uc.lp3.MPecci.Cs2.armas; para coincidir con el directorio.
  -Verificado: ./mvnw package BUILD SUCCESS (test en verde) y java -jar target/Cs2-0.0.1-SNAPSHOT.jar arranca Tomcat en el 8080.
  -Sobrecarga y sobreescritura:
  -Arma.java: nuevo método describir() (ancestro a sobreescribir).
  -Fusil.java: constructor sobrecargado (versión sin mira/retroceso/silenciador), método calcularDano() con 3 firmas, y @Override describir().
  -Humo.java: 3 constructores sobrecargados (con valores por defecto), areaCobertura() con 2 firmas, y @Override describir().
  .Verificado con ./mvnw compile y prueba interactiva en jshell (sobrecarga y despacho polimórfico de describir() funcionando).

2. **"Añadir sobrecarga y sobreescritura a las clases fusil y humo en
   Cs2/.../armas"** — se sumaron constructores sobrecargados y la sobrecarga de
   `calcularDano()` en `Fusil` y `areaCobertura()` en `Humo`; la sobrescritura de
   `describir()` en ambas clases, con el método concreto en la base `Arma`.
   Se verificó la compilación y se registró el cambio en un commit.

3. **"Publicá un servicio HTTP con Spring Boot sobre el modelado (CS2). Las
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