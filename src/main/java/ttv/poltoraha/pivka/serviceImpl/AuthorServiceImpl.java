package ttv.poltoraha.pivka.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.mapping.AuthorMapping;
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.service.AuthorService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapping authorMapping;

    @Override
    public void create(AuthorDto authorDto) {
        val author = authorMapping.toEntity(authorDto);
        authorRepository.save(author);
        authorMapping.toDto(author); // Note: Unused return value; consider returning or removing
    }

    @Override
    public void delete(Integer id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void addBooks(Integer id, List<Book> books) {
        val author = getOrThrow(id);
        author.getBooks().addAll(books);
        authorRepository.save(author); // Persist changes
    }

    @Override
    public void addBook(Integer id, Book book) {
        val author = getOrThrow(id);
        author.getBooks().add(book);
        authorRepository.save(author); // Persist changes
    }

    @Override
    public List<Author> getTopAuthorsByTag(String tag, int count) {
        val pageable = PageRequest.of(0, count);
        return authorRepository.findTopAuthorsByTag(tag, pageable);
    }

    private Author getOrThrow(Integer id) {
        val optionalAuthor = authorRepository.findById(id);
        val author = optionalAuthor.orElseThrow(() -> new RuntimeException("Author with id = " + id + " not found"));
        return author;
    }
}