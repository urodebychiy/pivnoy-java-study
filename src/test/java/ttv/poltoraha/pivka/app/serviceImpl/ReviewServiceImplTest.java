package ttv.poltoraha.pivka.app.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.entity.Review;
import ttv.poltoraha.pivka.entity.ReviewRating;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.repository.ReviewRatingRepository;
import ttv.poltoraha.pivka.repository.ReviewRepository;
import ttv.poltoraha.pivka.service.BookService;
import ttv.poltoraha.pivka.serviceImpl.ReviewServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @Mock
    private ReviewRatingRepository reviewRatingRepository;

    @Mock
    private ReaderRepository readerRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Reader reader;
    private Review review;
    private Book book;

    @BeforeEach
    void setUp() {
        reader = new Reader();
        reader.setUsername("testUser");

        book = new Book();
        book.setId(1);

        review = new Review();
        review.setId(1);
        review.setBook(book);
        review.setReaderUsername("testUser");
        review.setReviewRatings(new ArrayList<>());
    }

    @Test
    void createReviewRating_Success() {
        when(readerRepository.findByUsername("testUser")).thenReturn(Optional.of(reader));
        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(reviewRatingRepository.findByReaderUsernameAndReviewId("testUser", 1)).thenReturn(Optional.empty());
        when(reviewRatingRepository.save(any(ReviewRating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        reviewService.createReviewRating("testUser", 1, 5);

        verify(reviewRatingRepository).save(any(ReviewRating.class));
        verify(reviewRepository).save(review);
        assertThat(review.getReviewRatings()).hasSize(1);
        assertThat(review.getReviewRatings().get(0).getRating()).isEqualTo(5);
    }

    @Test
    void createReviewRating_InvalidRating_ThrowsException() {
//        when(readerRepository.findByUsername("testUser")).thenReturn(Optional.of(reader));
//        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> reviewService.createReviewRating("testUser", 1, 0));
        assertThat(exception.getMessage()).isEqualTo("Rating must be between 1 and 5");
    }

    @Test
    void createReviewRating_AlreadyRated_ThrowsException() {
        ReviewRating existingRating = new ReviewRating();
        existingRating.setReader(reader);
        existingRating.setReview(review);
        existingRating.setRating(3);

        when(readerRepository.findByUsername("testUser")).thenReturn(Optional.of(reader));
        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(reviewRatingRepository.findByReaderUsernameAndReviewId("testUser", 1)).thenReturn(Optional.of(existingRating));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> reviewService.createReviewRating("testUser", 1, 4));
        assertThat(exception.getMessage()).isEqualTo("User has already rated this review");
    }

    @Test
    void updateAvgRating_Success() {
        ReviewRating rating1 = new ReviewRating();
        rating1.setRating(3);
        ReviewRating rating2 = new ReviewRating();
        rating2.setRating(5);
        review.getReviewRatings().addAll(List.of(rating1, rating2));

        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);

        reviewService.updateAvgRating(1);

        verify(reviewRepository).save(review);
        assertThat(review.getAvgRating()).isEqualTo(4.0);
    }

    @Test
    void updateAvgRating_NoRatings_SetsZero() {
        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);

        reviewService.updateAvgRating(1);

        verify(reviewRepository).save(review);
        assertThat(review.getAvgRating()).isEqualTo(0.0);
    }

    @Test
    void createReviewRating_ReviewNotFound_ThrowsException() {
//        when(readerRepository.findByUsername("testUser")).thenReturn(Optional.of(reader));
        when(reviewRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> reviewService.createReviewRating("testUser", 1, 4));
        assertThat(exception.getMessage()).contains("review", "1");
    }
}
