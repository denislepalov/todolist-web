package lepdv.todolistweb.repository;

import lepdv.todolistweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
                                        FilterUserRepository,
                                        QuerydslPredicateExecutor<User> {

    Optional<User> findByUsername(String userName);

}
