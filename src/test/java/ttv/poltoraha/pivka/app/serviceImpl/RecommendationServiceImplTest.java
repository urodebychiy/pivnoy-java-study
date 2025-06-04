package ttv.poltoraha.pivka.app.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.repository.QuoteRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.repository.ReadingRepository;
import ttv.poltoraha.pivka.service.AuthorService;
import ttv.poltoraha.pivka.serviceImpl.RecommendationServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceImplTest {

    @Mock
    private ReaderRepository readerRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReadingRepository readingRepository;

    @Mock
    private QuoteRepository quoteRepository;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    private Book book;
    private Quote quote1;
    private Quote quote2;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1);

        quote1 = new Quote();
        quote1.setId(1);
        quote1.setBook(book);
        quote1.setAvgRating(4.5);

        quote2 = new Quote();
        quote2.setId(2);
        quote2.setBook(book);
        quote2.setAvgRating(3.0);
    }

    @Test
    void recommendQuoteByBook_Success_ReturnsSortedQuotes() {
        when(bookRepository.existsById(1)).thenReturn(true);
        when(quoteRepository.findTop5ByBookIdOrderByAvgRatingDesc(1)).thenReturn(List.of(quote1, quote2));

        List<Quote> result = recommendationService.recommendQuoteByBook(1);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getAvgRating()).isEqualTo(4.5);
        assertThat(result.get(1).getId()).isEqualTo(2);
        assertThat(result.get(1).getAvgRating()).isEqualTo(3.0);
        verify(quoteRepository).findTop5ByBookIdOrderByAvgRatingDesc(1);
    }

    @Test
    void recommendQuoteByBook_BookNotFound_ThrowsException() {
        when(bookRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> recommendationService.recommendQuoteByBook(1));
        assertThat(exception.getMessage()).contains("book", "1");
        verify(quoteRepository, never()).findTop5ByBookIdOrderByAvgRatingDesc(1);
    }

    @Test
    void recommendQuoteByBook_NoQuotes_ReturnsEmptyList() {
        when(bookRepository.existsById(1)).thenReturn(true);
        when(quoteRepository.findTop5ByBookIdOrderByAvgRatingDesc(1)).thenReturn(List.of());

        List<Quote> result = recommendationService.recommendQuoteByBook(1);

        assertThat(result).isEmpty();
        verify(quoteRepository).findTop5ByBookIdOrderByAvgRatingDesc(1);
    }
}