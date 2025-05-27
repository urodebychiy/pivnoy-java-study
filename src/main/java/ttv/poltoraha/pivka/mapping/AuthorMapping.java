package ttv.poltoraha.pivka.mapping;

import org.springframework.stereotype.Component;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;

@Component
public class AuthorMapping {

    public AuthorDto toDto(Author author) {
        return AuthorDto.builder()
                .fullName(author.getFullName())
                .avgRating(author.getAvgRating())
                .books(author.getBooks())
                .build();
    }

    public Author toEntity(AuthorDto authorDto) {
        return Author.builder()
                .fullName(authorDto.getFullName())
                .avgRating(authorDto.getAvgRating())
                .books(authorDto.getBooks())
                .build();
    }
}
