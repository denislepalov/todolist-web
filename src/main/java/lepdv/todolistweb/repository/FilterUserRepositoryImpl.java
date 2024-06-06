package lepdv.todolistweb.repository;


import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lepdv.todolistweb.dto.filter.UserFilter;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.querydsl.QPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import static lepdv.todolistweb.entity.QUser.user;


@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;


    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> findAllByFilter(UserFilter filter) {
        Predicate predicate = QPredicates.builder()
                .add(filter.username(), user.username::containsIgnoreCase)
                .add(filter.fullName(), user.fullName::containsIgnoreCase)
                .add(filter.dateOfBirth(), user.dateOfBirth::before)
                .buildAnd();

        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .orderBy(user.username.asc())
                .fetch();
    }
}
