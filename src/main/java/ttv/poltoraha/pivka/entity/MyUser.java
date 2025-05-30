package ttv.poltoraha.pivka.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name="app_user")
@Data
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class MyUser {
    @Id
    @Column(unique = true, nullable = false)
    private String username;
    private String password;

    @Column(name = "migrated_user")
    private boolean migratedUser;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
