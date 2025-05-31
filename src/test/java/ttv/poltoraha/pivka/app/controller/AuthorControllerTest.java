package ttv.poltoraha.pivka.app.controller;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ttv.poltoraha.pivka.controller.AuthorController;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.service.AuthorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {
    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author();
    }

    @Test
    void testCreateAuthor_success() {
        ResponseEntity<Void> response = authorController.createAuthor(author);

        assertEquals(200, response.getStatusCodeValue());
        verify(authorService).create(author);
    }

    @Test
    void testCreateAuthor_EntityNotFound() {
        doThrow(new EntityNotFoundException("Author not found")).when(authorService).create(author);

        ResponseEntity<Void> response = authorController.createAuthor(author);

        assertEquals(404, response.getStatusCodeValue());
        verify(authorService).create(author);
    }

    @Test
    void testCreateAuthor_InternalServerError() {
        doThrow(new RuntimeException("Unexpected error")).when(authorService).create(author);

        ResponseEntity<Void> response = authorController.createAuthor(author);

        assertEquals(500, response.getStatusCodeValue());
        verify(authorService).create(author);
    }
}
