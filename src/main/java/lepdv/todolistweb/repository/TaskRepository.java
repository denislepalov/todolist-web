package lepdv.todolistweb.repository;

import lepdv.todolistweb.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long>,
                                        FilterTaskRepository,
                                        QuerydslPredicateExecutor<Task> {

    List<Task> findAllByUserIdOrderById(Long id);




}
