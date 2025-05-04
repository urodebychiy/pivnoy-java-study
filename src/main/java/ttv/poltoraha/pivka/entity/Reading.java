package ttv.poltoraha.pivka.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    @ToString.Exclude
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private Book book;
}
