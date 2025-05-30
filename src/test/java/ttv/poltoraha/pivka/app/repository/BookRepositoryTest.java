package ttv.poltoraha.pivka.app.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.repository.BookRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {

        entityManager.clear();
    }

    @Test
    void testFindBooksByAuthorLastNameWithHighestRating() {

        Author author1 = Author.builder()
                .fullName("Иванов")
                .avgRating(4.5)
                .build();
        Author author2 = Author.builder()
                .fullName("Иванов")
                .avgRating(5.0)
                .build();
        Author author3 = Author.builder()
                .fullName("Петров")
                .avgRating(3.0)
                .build();

        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.persist(author3);


        Book book1 = Book.builder()
                .article("Book 1")
                .genre("Fiction")
                .rating(4.0)
                .author(author1)
                .build();
        Book book2 = Book.builder()
                .article("Book 2")
                .genre("Non-fiction")
                .rating(4.8)
                .author(author2)
                .build();
        Book book3 = Book.builder()
                .article("Book 3")
                .genre("Fiction")
                .rating(3.5)
                .author(author2)
                .build();
        Book book4 = Book.builder()
                .article("Book 4")
                .genre("Fiction")
                .rating(4.2)
                .author(author3)
                .build();


        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(book3);
        entityManager.persist(book4);
        entityManager.flush();


        List<Book> books = bookRepository.findBooksByAuthorLastName("%Иванов%");


        assertEquals(2, books.size(), "Должно быть найдено 2 книги автора с самым высоким рейтингом");
        assertTrue(books.stream().allMatch(book -> book.getAuthor().getAvgRating() == 5.0),
                "Все книги должны принадлежать автору с рейтингом 5.0");
        assertTrue(books.stream().anyMatch(book -> book.getArticle().equals("Book 2")),
                "Книга 'Book 2' должна быть в результате");
        assertTrue(books.stream().anyMatch(book -> book.getArticle().equals("Book 3")),
                "Книга 'Book 3' должна быть в результате");
    }
}
