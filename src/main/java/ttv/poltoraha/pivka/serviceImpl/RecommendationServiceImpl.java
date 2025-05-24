package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.entity.Reading;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.repository.ReadingRepository;
import ttv.poltoraha.pivka.service.AuthorService;
import ttv.poltoraha.pivka.service.RecommendationService;
import util.MyUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationServiceImpl implements RecommendationService {
    private final ReaderRepository readerRepository;
    private final AuthorService authorService;
    private final BookRepository bookRepository;
    private final ReadingRepository readingRepository;

    @Override
    public List<Author> recommendAuthor(String username) {
        val reader = MyUtility.findEntityById(readerRepository.findByUsername(username), "reader", username);

        val mostPopularTags = reader.getReadings().stream()
                .map(Reading::getBook) // Получаем книги
                .flatMap(book -> book.getTags().stream()) // Получаем теги из каждой книги
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting())) // Группируем теги и считаем их количество
                .entrySet()
                .stream() // Преобразуем в поток
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // Сортируем по количеству вхождений (по убыванию)
                .limit(2) // Берем только два самых популярных тега
                .map(Map.Entry::getKey) // Извлекаем только теги
                .toList(); // Собираем в список

        if (mostPopularTags.size() < 2) {
            // todo по-хорошему прокинуть ошибку, да и вообще сделать ControllerAdvice
            return null;
        }

        val mostRecommendedAuthor = authorService.getTopAuthorsByTag(mostPopularTags.get(0), 3);
        val secondMostRecommendedAuthor = authorService.getTopAuthorsByTag(mostPopularTags.get(1), 2);

        return Stream.concat(
                        mostRecommendedAuthor.stream(),
                        secondMostRecommendedAuthor.stream()
                )
                .distinct() // Убираем дубликаты, если это необходимо
                .toList(); // Собираем в список
    }

    /**
     * Чё должен делать метод
     * 1. Пользак заходит на сайт, хочет получить список книг, которые ему можно прочитать
     * 2. Мы берём все его прочитанные книги
     * 3. Отбираем среди них 2 самых популярных тега
     * 4. По первому популярному тэгу отбираем 3 книги с максимальным рейтингом
     * 5. По второму популярному тэгу отбираем 2 книги с максимальным рейтингом
     * 6. Возвращаем пользаку эти книжонку
     *
     * @return
     */
    @Override
    public List<Book> recommendBook(String username) {
        val reader = MyUtility.findEntityById(readerRepository.findByUsername(username), "reader", username);

        val readBooks = reader.getReadings().stream()
                .map(Reading::getBook)
                .toList(); // List<Book>

        val mostPopularTags = readBooks.stream()
                .flatMap(book -> book.getTags().stream().map(String::toLowerCase))
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(2)
                .map(Map.Entry::getKey)
                .toList();

        if (mostPopularTags.isEmpty()) {
            // todo: по-хорошему прокинуть ошибку через ControllerAdvice
            return Collections.emptyList();
        }

        val topBooksFirstTag = bookRepository.findByTagsContaining(mostPopularTags.get(0)).stream()
                .filter(book -> book.getRating() != null)
                .filter(book -> !readBooks.contains(book))
                .sorted(Comparator.comparing(Book::getRating).reversed())
                .limit(3)
                .toList(); // List<Book>

        val topBooksSecondTag = mostPopularTags.size() > 1
                ? bookRepository.findByTagsContaining(mostPopularTags.get(1)).stream()
                .filter(book -> book.getRating() != null)
                .filter(book -> !readBooks.contains(book))
                .sorted(Comparator.comparing(Book::getRating).reversed())
                .limit(2)
                .toList() // List<Book>
                : new ArrayList<Book>();

        // 6. Объединяем результаты в ArrayList<Book>
        val result = new ArrayList<Book>();
        result.addAll(topBooksFirstTag);
        result.addAll(topBooksSecondTag);

        // Удаляем дубликаты вручную
        val distinctResult = new ArrayList<Book>();
        val seenIds = new HashSet<Integer>();
        for (Book book : result) {
            if (seenIds.add(book.getId())) {
                distinctResult.add(book);
            }
        }

        return Collections.unmodifiableList(distinctResult);
    }

    @Override
    public List<Quote> recommendQuoteByBook(Integer book_id) {
        if (!bookRepository.existsById(book_id)) {
            throw new EntityNotFoundException(String.format("Entity book with id = %s was not found", book_id));
        }

        val readings = readingRepository.findAllByBook_id(book_id);

        val topReader = readings.stream()
                .map(Reading::getReader)
                .sorted(Comparator.comparingInt(reader -> reader.getReadings().size()))
                .limit(5)
                .toList();

        return topReader.stream()
                .flatMap(reader -> reader.getQuotes().stream())
                .filter(quote -> Objects.equals(quote.getBook().getId(), book_id))
                .toList();
    }
}
