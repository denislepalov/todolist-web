package lepdv.todolistweb.service;

import com.querydsl.core.types.Predicate;
import lepdv.todolistweb.dto.filter.TaskFilterForAuthUser;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.querydsl.QPredicates;
import lepdv.todolistweb.repository.TaskRepository;
import lepdv.todolistweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static lepdv.todolistweb.entity.QTask.task;
import static lepdv.todolistweb.util.AuthUser.getAuthUsername;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllByUserId(Long id) { return taskRepository.findAllByUserIdOrderById(id); }

    public List<Task> getAllByAuthUserByFilter(TaskFilterForAuthUser filter) {
        return taskRepository.findAllByAuthUserByFilter(filter);
    }


    public Page<Task> getTaskPageByAuthUserByFilter(TaskFilterForAuthUser filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(getAuthUsername(), task.user.username::eq)
                .add(filter.dueDate(), task.dueDate::before)
                .add(filter.isCompleted(), task.isCompleted::eq)
                .buildAnd();
        return taskRepository.findAll(predicate, pageable);
    }


    @Transactional
    public void create(Task task, Long userId) {
        task.setDateOfCreation(LocalDate.now());
        task.setIsCompleted("Not completed");
        User user = userRepository.findById(userId).get();
        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        log.info("New task was created id={}", savedTask.getId());
    }

    @Transactional
    public void update(Task updatingTask, Long id) {
        Task updatedTask = taskRepository.findById(id).get();
        updatedTask.setDescription(updatingTask.getDescription());
        updatedTask.setDueDate(updatingTask.getDueDate());
        log.info("Task id={} was updated", id);
    }


    @Transactional
    public void markAsCompleted(Long id) {
        Task task = taskRepository.findById(id).get();
        task.setIsCompleted("Completed");
        log.info("Task id={} was marked as completed", id);
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
        log.info("Task id={} was deleted", id);
    }
}
