package py.edu.uc.lp3.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.edu.uc.lp3.domain.Arma;

@Repository
public interface ArmaRepository extends JpaRepository<Arma, Integer> {

    List<Arma> findByNombreContainingIgnoreCase(String nombre);

    List<Arma> findByPrecioLessThanEqual(double precio);

    List<Arma> findByDurabilidadLessThan(int durabilidad);
}
