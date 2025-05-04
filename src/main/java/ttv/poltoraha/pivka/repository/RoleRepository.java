package ttv.poltoraha.pivka.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
