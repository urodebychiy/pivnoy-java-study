package ttv.poltoraha.pivka.service;

public interface ReaderService {
    public void createQuote(String username, Integer book_id, String text);
    public void addFinishedBook(String username, Integer bookId);
    public void createReader(String username, String password);
}
