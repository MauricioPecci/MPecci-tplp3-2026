package py.edu.uc.lp3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.edu.uc.lp3.entity.ArmaEntity;

@Repository
public interface ArmaRepository extends JpaRepository<ArmaEntity, Long> {
}
