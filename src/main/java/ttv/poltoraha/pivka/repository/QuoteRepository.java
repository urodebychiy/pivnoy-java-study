package ttv.poltoraha.pivka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Quote;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
