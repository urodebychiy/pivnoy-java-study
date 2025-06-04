package ttv.poltoraha.pivka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.ReviewRating;

import java.util.Optional;

@Repository
public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long> {
    Optional<ReviewRating> findByReaderUsernameAndReviewId(String readerUsername, Integer reviewId);
}
