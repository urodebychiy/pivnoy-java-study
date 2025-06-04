package ttv.poltoraha.pivka.service;

public interface QuoteService {
    public void updateAvgRating(Integer quoteId);
    public void createQuoteRating(String username, Integer quoteId, Integer rating);

}
