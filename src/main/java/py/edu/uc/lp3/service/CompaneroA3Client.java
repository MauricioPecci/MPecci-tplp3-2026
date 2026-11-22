package py.edu.uc.lp3.service;

import java.util.Map;
import py.edu.uc.lp3.rest.dto.RafagaA3Request;

public interface CompaneroA3Client {

    Map<String, Object> validarRafaga(RafagaA3Request request);
}
