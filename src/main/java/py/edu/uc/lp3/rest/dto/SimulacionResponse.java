package py.edu.uc.lp3.rest.dto;

public record SimulacionResponse(
        String atacanteClase,
        int atacanteMunicionAntes,
        int atacanteMunicionDespues,
        String objetivoClase,
        int objetivoDurabilidadAntes,
        int objetivoDurabilidadDespues,
        String recargadorClase,
        int recargadorMunicionAntes,
        int recargadorMunicionDespues,
        int danoAplicado,
        int municionRecargada) {
}
