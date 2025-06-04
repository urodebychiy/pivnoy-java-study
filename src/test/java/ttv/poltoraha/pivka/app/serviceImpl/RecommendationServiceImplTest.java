package ttv.poltoraha.pivka.app.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.controller.ReaderController;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.entity.Reading;
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.repository.ReadingRepository;
import ttv.poltoraha.pivka.service.ReaderService;
import ttv.poltoraha.pivka.service.RecommendationService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ttv.poltoraha.pivka.app.util.TestConst.USERNAME;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Используйте H2 вместо реальной БД
@Transactional // Обеспечивает откат транзакций после каждого теста
class RecommendationServiceImplTest {
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReaderController readerController;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ReadingRepository readingRepository;

    private Reader reader;
    private Book book;
    private Author author;
    private Reading reading;

    @BeforeEach
    void setUp() {

        reader = new Reader();
        reader.setUsername(USERNAME);
        reader.setPassword("132");

        readerRepository.save(reader);


        author = new Author();
        author.setId(1);

        authorRepository.save(author);


        book = new Book();
        book.setId(1);
        book.setAuthor(author);

        bookRepository.save(book);


        reading = new Reading();
        reading.setReader(reader);
        reading.setBook(book);

        readingRepository.save(reading);



        readerService.addFinishedBook(USERNAME, 1);

        readerController.addQuote(USERNAME, 1, "quote");
    }

    @Test
    void checkRecommendQuote() {
        List<Quote> quotes = recommendationService.recommendQuoteByBook(1);

        assertEquals(1, quotes.size(), "Ожидается одна цитата");
        assertEquals("quote", quotes.get(0).getText(), "Текст цитаты должен быть 'quote'");
    }

    @Test
    void whenBookDoesNotFound_thenThrowEntityNotFoundException() {
        assertThatThrownBy(() -> recommendationService.recommendQuoteByBook(999))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Entity book with id = 999 was not found");
    }

    @Test
    void whenReaderHasNotQuotes_thenReturnEmptyList() {
        reader.setQuotes(new ArrayList<>());
        readerRepository.save(reader);

        List<Quote> quotes = recommendationService.recommendQuoteByBook(1);

        assertTrue(quotes.isEmpty(), "У вас нет цитат");
    }

    // я моб, слава нейросетям, тесты в рот ебал писать
}
