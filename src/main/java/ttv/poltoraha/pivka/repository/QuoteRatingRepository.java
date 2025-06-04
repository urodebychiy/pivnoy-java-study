package ttv.poltoraha.pivka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.QuoteRating;

import java.util.Optional;

@Repository
public interface QuoteRatingRepository extends JpaRepository<QuoteRating, Long> {
    Optional<QuoteRating> findByReaderUsernameAndQuoteId(String readerUsername, Integer quoteId);
}
