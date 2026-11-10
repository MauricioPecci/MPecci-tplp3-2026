package py.edu.uc.lp3.rest.dto;

public record SimulacionRequest(
        Integer atacanteId,
        Integer objetivoId,
        Integer recargadorId,
        int distancia,
        boolean critico,
        int municion) {
}
