package py.edu.uc.lp3.rest.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.Equipable;
import py.edu.uc.lp3.domain.Escopeta;
import py.edu.uc.lp3.domain.Flash;
import py.edu.uc.lp3.domain.Francotirador;
import py.edu.uc.lp3.domain.Fusil;
import py.edu.uc.lp3.domain.Granada;
import py.edu.uc.lp3.domain.Humo;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.domain.UIcapable;

@RestController
@RequestMapping("/api/armas")
public class ArmaController {

    @GetMapping("/descripcion")
    public List<Map<String, Object>> descripcionDeLasClasesHijas() {
        List<Arma> armas = List.of(
                new Fusil("AK-47", 1, 2700, 36, 71, 2.4, 600, true, 2, 3, false),
                new Humo("Humo de cortina", 8, 300));

        return armas.stream()
                .map(arma -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("clase", arma.getClass().getSimpleName());
                    item.put("heredaDe", Arma.class.getSimpleName());
                    item.put("descripcion", arma.describir());
                    return item;
                })
                .toList();
    }

    @GetMapping("/equipables")
    public List<Map<String, Object>> mensajeDeEquipadoATravesDeLaInterfaz() {
        List<Equipable> equipables = List.of(
                new Fusil("AK-47", 1, 2700, 36, 71, 2.4, 600, true, 2, 3, false),
                new Granada("Granada HE", 5, 300, "explosiva", 4.0, 30.0, 1.0, 98, false));

        return equipables.stream()
                .map(equipable -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("clase", equipable.getClass().getSimpleName());
                    item.put("tipoDeInterfaz", Equipable.class.getSimpleName());
                    item.put("ranura", equipable.ranura());
                    item.put("mensaje", equipable.mensajeDeEquipado());
                    return item;
                })
                .toList();
    }

    @GetMapping("/ui")
    public List<Map<String, Object>> imagenYSonidoDeTodasLasClases() {
        List<UIcapable> armas = List.of(
                new Fusil("AK-47", 1, 2700, 36, 71, 2.4, 600, true, 2, 3, false),
                new Escopeta("XM1014", 2, 2000, 20, 55, 3.0, 171, 8, 4.5),
                new Pistola("USP-S", 3, 200, 35, 70, 1.5, 352, false, true),
                new Francotirador("AWP", 4, 4750, 115, 85, 3.7, 340, false, 8, 5, true, 80.0, 4),
                new Humo("Humo de cortina", 5, 300),
                new Granada("Granada HE", 6, 300, "explosiva", 4.0, 30.0, 1.0, 98, false),
                new Flash("Flashbang", 7, 200, "cegadora", 5.0, 20.0, 2.0, 100, 4.5));

        return armas.stream()
                .map(ui -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("clase", ui.getClass().getSimpleName());
                    item.put("imagen", ui.imagen());
                    item.put("sonido", ui.sonido());
                    return item;
                })
                .toList();
    }

    @GetMapping("/fusil")
    public Map<String, Object> construirFusil(
            @RequestParam(defaultValue = "AK-47") String nombre,
            @RequestParam(defaultValue = "1") int id,
            @RequestParam(defaultValue = "2700") double precio,
            @RequestParam(defaultValue = "36") int dano,
            @RequestParam(defaultValue = "71") int precision,
            @RequestParam(defaultValue = "2.4") double recarga,
            @RequestParam(defaultValue = "600") double velocidad,
            @RequestParam(required = false) Boolean automatica,
            @RequestParam(required = false) Integer mira,
            @RequestParam(required = false) Integer retroceso,
            @RequestParam(required = false) Boolean silenciador,
            @RequestParam(required = false) Integer distancia,
            @RequestParam(required = false) Boolean critico) {

        boolean conAccesorios = mira != null || retroceso != null || silenciador != null;
        Fusil fusil = conAccesorios
                ? new Fusil(nombre, id, precio, dano, precision, recarga, velocidad,
                        Boolean.TRUE.equals(automatica),
                        mira != null ? mira : 1,
                        retroceso != null ? retroceso : 0,
                        Boolean.TRUE.equals(silenciador))
                : new Fusil(nombre, id, precio, dano, precision, recarga, velocidad,
                        Boolean.TRUE.equals(automatica));

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("clase", fusil.getClass().getSimpleName());
        respuesta.put("descripcion", fusil.describir());
        if (distancia != null && critico != null) {
            respuesta.put("dano", fusil.calcularDano(distancia, critico));
        } else if (distancia != null) {
            respuesta.put("dano", fusil.calcularDano(distancia));
        } else {
            respuesta.put("dano", fusil.calcularDano());
        }
        respuesta.put("sobrecargaDeCalcularDano",
                distancia == null ? "sin distancia" : (critico == null ? "distancia" : "distancia + crítico"));
        return respuesta;
    }

    @GetMapping("/humo")
    public Map<String, Object> construirHumo(
            @RequestParam(defaultValue = "Humo de cortina") String nombre,
            @RequestParam(defaultValue = "8") int id,
            @RequestParam(defaultValue = "300") double precio,
            @RequestParam(required = false) Double radio,
            @RequestParam(required = false) Double radioExtra,
            @RequestParam(required = false) Double distancia,
            @RequestParam(required = false) Double duracion) {

        Humo humo;
        if (distancia != null || duracion != null) {
            humo = new Humo(nombre, id, precio, "humo",
                    radio != null ? radio : 6.0,
                    distancia != null ? distancia : 25.0,
                    duracion != null ? duracion : 18.0);
        } else if (radio != null) {
            humo = new Humo(nombre, id, precio, radio);
        } else {
            humo = new Humo(nombre, id, precio);
        }

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("clase", humo.getClass().getSimpleName());
        respuesta.put("descripcion", humo.describir());
        respuesta.put("areaCobertura", radioExtra != null
                ? humo.areaCobertura(radioExtra)
                : humo.areaCobertura());
        respuesta.put("sobrecargaDeAreaCobertura",
                radioExtra == null ? "sin radio extra" : "con radio extra");
        return respuesta;
    }
}
