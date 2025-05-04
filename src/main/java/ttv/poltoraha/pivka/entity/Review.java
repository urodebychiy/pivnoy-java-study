package ttv.poltoraha.pivka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="review")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String readerUsername;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private String text;
    private Integer rating;
}
