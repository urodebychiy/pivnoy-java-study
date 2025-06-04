package ttv.poltoraha.pivka.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Entity(name = "quote_rating")
@Data
public class QuoteRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "quote_id")
    private Quote quote;

    private Integer rating;

    @Column(unique = true, name = "reader_quote_unique")
    private String readerQuoteUnique;

    @PrePersist
    @PreUpdate
    private void ensureUniqueConstraint() {
        this.readerQuoteUnique = reader.getUsername() + "_" + quote.getId();
    }
}
