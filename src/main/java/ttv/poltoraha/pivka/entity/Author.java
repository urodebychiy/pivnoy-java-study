package ttv.poltoraha.pivka.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

import java.util.List;

// Энтити - это привязка класса к конкретной табличке в БД
@Entity(name="author")
@Data
@ToString
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName;
    private Double avgRating;
    @OneToMany(mappedBy="author")
    @ToString.Exclude
    private List<Book> books;

    @OneToMany(mappedBy = "author")
    private List<Pivo> pivos;
}
