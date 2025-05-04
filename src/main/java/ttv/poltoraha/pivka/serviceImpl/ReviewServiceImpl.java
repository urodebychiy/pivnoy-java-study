package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.dao.request.ReviewRequestDto;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Review;
import ttv.poltoraha.pivka.mapping.MappingUtil;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.repository.ReviewRepository;
import ttv.poltoraha.pivka.service.BookService;
import ttv.poltoraha.pivka.service.ReviewService;
import util.MyUtility;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Override
    public void createReview(ReviewRequestDto requestDto) {
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Entity with id = %d not found", requestDto.getBookId())));

        Review mappedReview = MappingUtil.fromRequestDto(requestDto, book);

        reviewRepository.save(mappedReview);

        bookService.updateAvgRating(requestDto.getBookId());
        // после подобной операции нужно обновить avgRating у Book
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
}
