package py.edu.uc.lp3.service;

import java.util.Map;
import py.edu.uc.lp3.rest.dto.ExplosionA2Request;

public interface CompaneroA2Client {

    Map<String, Object> validarExplosion(ExplosionA2Request request);
}
