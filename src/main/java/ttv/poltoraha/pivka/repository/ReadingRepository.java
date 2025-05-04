package ttv.poltoraha.pivka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttv.poltoraha.pivka.entity.Reading;

import java.util.List;

public interface ReadingRepository extends JpaRepository<Reading, Long> {
    List<Reading> findAllByBook_id(Integer bookId);
}
