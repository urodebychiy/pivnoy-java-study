package ttv.poltoraha.pivka.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Review;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.service.BookService;
import util.MyUtility;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public void updateAvgRating(Integer bookId) {
        Book book = MyUtility.findEntityById(bookRepository.findById(bookId), "book", bookId.toString());
        Double updatedAvgRaiting = book.getReviews()
                .stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        book.setRating(updatedAvgRaiting);

        bookRepository.save(book);
    }
}
