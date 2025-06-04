package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.dao.request.ReviewRequestDto;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.entity.Review;
import ttv.poltoraha.pivka.entity.ReviewRating;
import ttv.poltoraha.pivka.mapping.MappingUtil;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.repository.ReviewRatingRepository;
import ttv.poltoraha.pivka.repository.ReviewRepository;
import ttv.poltoraha.pivka.service.BookService;
import ttv.poltoraha.pivka.service.ReviewService;
import util.MyUtility;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final ReviewRatingRepository reviewRatingRepository;
    private final ReaderRepository readerRepository;

    @Override
    public void createReview(ReviewRequestDto requestDto) {
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Entity with id = %d not found", requestDto.getBookId())));

        Review mappedReview = MappingUtil.fromRequestDto(requestDto, book);

        reviewRepository.save(mappedReview);

        bookService.updateAvgRating(requestDto.getBookId());
    }

    @Override
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public void updateReview(Integer reviewId, ReviewRequestDto requestDto) {
        Review review = MyUtility.findEntityById(reviewRepository.findById(reviewId), "review", reviewId.toString());
        Review updatedReview = MappingUtil.updateFromRequestDto(review, requestDto);

        reviewRepository.save(updatedReview);

        bookService.updateAvgRating(requestDto.getBookId());
    }

    public void updateAvgRating(Integer reviewId) {
        Review review = MyUtility.findEntityById(reviewRepository.findById(reviewId), "review", reviewId.toString());
        Double updatedAvgRating = review.getReviewRatings()
                .stream()
                .mapToInt(ReviewRating::getRating)
                .average()
                .orElse(0.0);
        review.setAvgRating(updatedAvgRating);
        reviewRepository.save(review);
    }

    public void createReviewRating(String username, Integer reviewId, Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = MyUtility.findEntityById(reviewRepository.findById(reviewId), "review", reviewId.toString());
        Reader reader = MyUtility.findEntityById(readerRepository.findByUsername(username), "reader", username);

        Optional<ReviewRating> existingRating = reviewRatingRepository.findByReaderUsernameAndReviewId(username, reviewId);
        if (existingRating.isPresent()) {
            throw new IllegalStateException("User has already rated this review");
        }

        ReviewRating reviewRating = new ReviewRating();
        reviewRating.setReader(reader);
        reviewRating.setReview(review);
        reviewRating.setRating(rating);

        review.getReviewRatings().add(reviewRating);
        reviewRatingRepository.save(reviewRating);
        updateAvgRating(reviewId);
    }
}
