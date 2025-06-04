package ttv.poltoraha.pivka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Pivo;

@Repository
public interface PivoRepository extends JpaRepository<Pivo, Long> {
}
