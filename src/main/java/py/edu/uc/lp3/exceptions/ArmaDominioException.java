package py.edu.uc.lp3.exceptions;

public class ArmaDominioException extends RuntimeException {

    public ArmaDominioException(String mensaje) {
        super(mensaje);
    }

    public ArmaDominioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
