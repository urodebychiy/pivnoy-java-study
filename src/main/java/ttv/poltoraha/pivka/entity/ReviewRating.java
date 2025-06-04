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

@Entity(name = "review_rating")
@Data
public class ReviewRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private Integer rating;

    @Column(unique = true, name = "reader_review_unique")
    private String readerReviewUnique;

    @PrePersist
    @PreUpdate
    private void ensureUniqueConstraint() {
        this.readerReviewUnique = reader.getUsername() + "_" + review.getId();
    }
}
