package py.edu.uc.lp3.exceptions;

public class ValidacionCompaneroRechazadaException extends RuntimeException {

    private final String companero;
    private final int estadoExterno;
    private final String cuerpoExterno;

    public ValidacionCompaneroRechazadaException(String companero, int estadoExterno, String cuerpoExterno) {
        super("La regla de " + companero + " rechazó la operación: " + cuerpoExterno);
        this.companero = companero;
        this.estadoExterno = estadoExterno;
        this.cuerpoExterno = cuerpoExterno;
    }

    public String getCompanero() {
        return companero;
    }

    public int getEstadoExterno() {
        return estadoExterno;
    }

    public String getCuerpoExterno() {
        return cuerpoExterno;
    }
}
