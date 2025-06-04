package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAuthors(@RequestParam String username) {
        List<Author> authors = recommendationService.recommendAuthor(username);
        if (authors == null || authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks(@RequestParam String username) {
        List<Book> books = recommendationService.recommendBook(username);
        if (books == null || books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/quotes")
    public ResponseEntity<List<Quote>> getQuotes(@RequestParam Integer bookId) {
        List<Quote> quotes = recommendationService.recommendQuoteByBook(bookId);
        if (quotes == null || quotes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(quotes);
    }

}
