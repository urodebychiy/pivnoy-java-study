package ttv.poltoraha.pivka.app.serviceImpl;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.mapping.AuthorMapping;
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.serviceImpl.AuthorServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapping authorMapping;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private AuthorDto authorDto;
    private Author author;
    private Book book;
    private List<Book> books;
    private final Integer authorId = 1;

    @BeforeEach
    void setUp() {
        val fullName = "John Doe";
        val avgRating = 4.5;

        authorDto = AuthorDto.builder()
                .fullName(fullName)
                .avgRating(avgRating)
                .books(new ArrayList<>()) // Mutable list
                .build();

        author = Author.builder()
                .id(authorId)
                .fullName(fullName)
                .avgRating(avgRating)
                .books(new ArrayList<>()) // Mutable list
                .build();

        book = Book.builder()
                .id(1)
                .article("ART123")
                .genre("Fiction")
                .rating(4.0)
                .tags("fiction, adventure")
                .author(author)
                .build();

        books = new ArrayList<>(List.of(book)); // Mutable list
    }

    @Test
    void create_ShouldSaveAuthorAndMapToDto() {
        when(authorMapping.toEntity(authorDto)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapping.toDto(author)).thenReturn(authorDto);


        authorService.create(authorDto);


        verify(authorRepository, times(1)).save(author);
        verify(authorMapping, times(1)).toEntity(authorDto);
        verify(authorMapping, times(1)).toDto(author);
    }

    @Test
    void delete_ShouldCallDeleteById() {

        authorService.delete(authorId);


        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    void addBooks_ShouldAddBooksToAuthor_WhenAuthorExists() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);


        authorService.addBooks(authorId, books);


        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, times(1)).save(author);
        assertEquals(1, author.getBooks().size());
        assertTrue(author.getBooks().contains(book));
    }

    @Test
    void addBooks_ShouldThrowException_WhenAuthorNotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());


        val exception = assertThrows(RuntimeException.class, () -> authorService.addBooks(authorId, books));
        assertEquals("Author with id = " + authorId + " not found", exception.getMessage());
        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, never()).save(any());
    }

    @Test
    void addBook_ShouldAddBookToAuthor_WhenAuthorExists() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);


        authorService.addBook(authorId, book);


        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, times(1)).save(author);
        assertEquals(1, author.getBooks().size());
        assertTrue(author.getBooks().contains(book));
    }

    @Test
    void addBook_ShouldThrowException_WhenAuthorNotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());


        val exception = assertThrows(RuntimeException.class, () -> authorService.addBook(authorId, book));
        assertEquals("Author with id = " + authorId + " not found", exception.getMessage());
        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, never()).save(any());
    }

    @Test
    void getTopAuthorsByTag_ShouldReturnTopAuthors() {
        val tag = "fiction";
        val count = 5;
        val pageable = PageRequest.of(0, count);
        val authors = List.of(author);
        when(authorRepository.findTopAuthorsByTag(tag, pageable)).thenReturn(authors);


        val result = authorService.getTopAuthorsByTag(tag, count);


        verify(authorRepository, times(1)).findTopAuthorsByTag(tag, pageable);
        assertEquals(authors, result);
    }

    @Test
    void getTopAuthorsByTag_ShouldHandleEmptyResult() {
        val tag = "fiction";
        val count = 5;
        val pageable = PageRequest.of(0, count);
        when(authorRepository.findTopAuthorsByTag(tag, pageable)).thenReturn(List.of());


        val result = authorService.getTopAuthorsByTag(tag, count);


        verify(authorRepository, times(1)).findTopAuthorsByTag(tag, pageable);
        assertTrue(result.isEmpty());
    }
}