package ttv.poltoraha.pivka.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.MyUser;

@Repository
public interface MyUserRepository extends CrudRepository<MyUser, String> {
    MyUser findByUsername(String username);
}
