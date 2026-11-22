package py.edu.uc.lp3.exceptions;

public class CompaneroNoDisponibleException extends RuntimeException {

    private final String companero;

    public CompaneroNoDisponibleException(String companero, Throwable causa) {
        super("No fue posible consultar al compañero " + companero, causa);
        this.companero = companero;
    }

    public String getCompanero() {
        return companero;
    }
}
