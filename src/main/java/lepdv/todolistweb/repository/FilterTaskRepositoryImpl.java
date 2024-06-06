package lepdv.todolistweb.repository;


import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lepdv.todolistweb.dto.filter.TaskFilterForAdmin;
import lepdv.todolistweb.dto.filter.TaskFilterForAuthUser;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.querydsl.QPredicates;
import lepdv.todolistweb.util.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import static lepdv.todolistweb.entity.QTask.task;


@RequiredArgsConstructor
public class FilterTaskRepositoryImpl implements FilterTaskRepository {

    private final EntityManager entityManager;


    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Task> findAllByFilter(TaskFilterForAdmin filter) {
        Predicate predicate = QPredicates.builder()
                .add(filter.user(), task.user.username::containsIgnoreCase)
                .add(filter.dateOfCreation(), task.dateOfCreation::before)
                .buildAnd();

        return new JPAQuery<Task>(entityManager)
                .select(task)
                .from(task)
                .where(predicate)
                .orderBy(task.user.username.asc(), task.id.asc())
                .fetch();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Task> findAllByFilterByPageable(TaskFilterForAdmin filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.user(), task.user.username::containsIgnoreCase)
                .add(filter.dateOfCreation(), task.dateOfCreation::before)
                .buildAnd();

        List<Task> taskList = new JPAQuery<Task>(entityManager)
                .select(task)
                .from(task)
                .where(predicate)
                .orderBy(task.user.username.asc(), task.id.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        return new PageImpl<>(taskList, pageable, taskList.size());
    }


    @Override
    public List<Task> findAllByAuthUserByFilter(TaskFilterForAuthUser filter) {
        Predicate predicate = QPredicates.builder()
                .add(filter.dueDate(), task.dueDate::before)
                .add(filter.isCompleted(), task.isCompleted::equalsIgnoreCase)
                .buildAnd();

        return new JPAQuery<Task>(entityManager)
                .select(task)
                .from(task)
                .where(task.user.username.eq(AuthUser.getAuthUsername()).and(predicate))
                .orderBy(task.id.asc())
                .fetch();
    }

}
