package ttv.poltoraha.pivka.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.entity.QuoteRating;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.repository.QuoteRatingRepository;
import ttv.poltoraha.pivka.repository.QuoteRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.service.QuoteService;
import util.MyUtility;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;
    private final QuoteRatingRepository quoteRatingRepository;
    private final ReaderRepository readerRepository;

    public void updateAvgRating(Integer quoteId) {
        Quote quote = MyUtility.findEntityById(quoteRepository.findById(quoteId), "quote", quoteId.toString());
        Double updatedAvgRating = quote.getQuoteRatings()
                .stream()
                .mapToInt(QuoteRating::getRating)
                .average()
                .orElse(0.0);
        quote.setAvgRating(updatedAvgRating);
        quoteRepository.save(quote);
    }

    public void createQuoteRating(String username, Integer quoteId, Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Quote quote = MyUtility.findEntityById(quoteRepository.findById(quoteId), "quote", quoteId.toString());
        Reader reader = MyUtility.findEntityById(readerRepository.findByUsername(username), "reader", username);

        Optional<QuoteRating> existingRating = quoteRatingRepository.findByReaderUsernameAndQuoteId(username, quoteId);
        if (existingRating.isPresent()) {
            throw new IllegalStateException("User has already rated this quote");
        }

        QuoteRating quoteRating = new QuoteRating();
        quoteRating.setReader(reader);
        quoteRating.setQuote(quote);
        quoteRating.setRating(rating);

        quote.getQuoteRatings().add(quoteRating);
        quoteRatingRepository.save(quoteRating);
        updateAvgRating(quoteId);
    }
}
