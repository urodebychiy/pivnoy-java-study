package ttv.poltoraha.pivka.app.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.entity.QuoteRating;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.repository.QuoteRatingRepository;
import ttv.poltoraha.pivka.repository.QuoteRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.serviceImpl.QuoteServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteServiceImplTest {

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private QuoteRatingRepository quoteRatingRepository;

    @Mock
    private ReaderRepository readerRepository;

    @InjectMocks
    private QuoteServiceImpl quoteService;

    private Reader reader;
    private Quote quote;

    @BeforeEach
    void setUp() {
        reader = new Reader();
        reader.setUsername("testUser");
        reader.setQuotes(new ArrayList<>());

        quote = new Quote();
        quote.setId(1);
        quote.setReader(reader);
        quote.setQuoteRatings(new ArrayList<>());
    }

    @Test
    void createQuoteRating_Success() {
        when(readerRepository.findByUsername("testUser")).thenReturn(Optional.of(reader));
        when(quoteRepository.findById(1)).thenReturn(Optional.of(quote));
        when(quoteRatingRepository.findByReaderUsernameAndQuoteId("testUser", 1)).thenReturn(Optional.empty());
        when(quoteRatingRepository.save(any(QuoteRating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        quoteService.createQuoteRating("testUser", 1, 4);

        verify(quoteRatingRepository).save(any(QuoteRating.class));
        verify(quoteRepository).save(quote);
        assertThat(quote.getQuoteRatings()).hasSize(1);
        assertThat(quote.getQuoteRatings().get(0).getRating()).isEqualTo(4);
    }

    @Test
    void createQuoteRating_InvalidRating_ThrowsException() {
//        when(readerRepository.findByUsername("testUser")).thenReturn(Optional.of(reader));
//        when(quoteRepository.findById(1)).thenReturn(Optional.of(quote));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> quoteService.createQuoteRating("testUser", 1, 6));
        assertThat(exception.getMessage()).isEqualTo("Rating must be between 1 and 5");
    }

    @Test
    void createQuoteRating_AlreadyRated_ThrowsException() {
        QuoteRating existingRating = new QuoteRating();
        existingRating.setReader(reader);
        existingRating.setQuote(quote);
        existingRating.setRating(3);

        when(readerRepository.findByUsername("testUser")).thenReturn(Optional.of(reader));
        when(quoteRepository.findById(1)).thenReturn(Optional.of(quote));
        when(quoteRatingRepository.findByReaderUsernameAndQuoteId("testUser", 1)).thenReturn(Optional.of(existingRating));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> quoteService.createQuoteRating("testUser", 1, 4));
        assertThat(exception.getMessage()).isEqualTo("User has already rated this quote");
    }

    @Test
    void updateAvgRating_Success() {
        QuoteRating rating1 = new QuoteRating();
        rating1.setRating(4);
        QuoteRating rating2 = new QuoteRating();
        rating2.setRating(2);
        quote.getQuoteRatings().addAll(List.of(rating1, rating2));

        when(quoteRepository.findById(1)).thenReturn(Optional.of(quote));
        when(quoteRepository.save(quote)).thenReturn(quote);

        quoteService.updateAvgRating(1);

        verify(quoteRepository).save(quote);
        assertThat(quote.getAvgRating()).isEqualTo(3.0);
    }

    @Test
    void updateAvgRating_NoRatings_SetsZero() {
        when(quoteRepository.findById(1)).thenReturn(Optional.of(quote));
        when(quoteRepository.save(quote)).thenReturn(quote);

        quoteService.updateAvgRating(1);

        verify(quoteRepository).save(quote);
        assertThat(quote.getAvgRating()).isEqualTo(0.0);
    }

    @Test
    void createQuoteRating_QuoteNotFound_ThrowsException() {
//        when(readerRepository.findByUsername("testUser")).thenReturn(Optional.of(reader));
        when(quoteRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> quoteService.createQuoteRating("testUser", 1, 4));
        assertThat(exception.getMessage()).contains("quote", "1");
    }
}
