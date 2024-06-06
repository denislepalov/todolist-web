package lepdv.todolistweb.service;

import com.querydsl.core.types.Predicate;
import lepdv.todolistweb.dto.filter.TaskFilterForAdmin;
import lepdv.todolistweb.dto.filter.UserFilter;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.querydsl.QPredicates;
import lepdv.todolistweb.repository.TaskRepository;
import lepdv.todolistweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static lepdv.todolistweb.entity.QTask.task;
import static lepdv.todolistweb.entity.QUser.user;


@Service
@Transactional(readOnly = true)
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "username"));
    }


    public List<User> getAllUsersByFilter(UserFilter filter) {
        return userRepository.findAllByFilter(filter);
    }


    public Page<User> getUserPageByFilter(UserFilter filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.username(), user.username::containsIgnoreCase)
                .add(filter.fullName(), user.fullName::containsIgnoreCase)
                .add(filter.dateOfBirth(), user.dateOfBirth::before)
                .buildAnd();
        return userRepository.findAll(predicate, pageable);
    }


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    /**
     used method (from FilterTaskRepository)
     filtration; sorting by (Task.user.username, Task.id); pagination.
     */
    public Page<Task> getAllTasksByFilterByPageable(TaskFilterForAdmin filter, Pageable pageable) {
        return taskRepository.findAllByFilterByPageable(filter, pageable);
    }


    /**
     spare method (from FilterTaskRepository)
     filtration; sorting by (Task.user.username, Task.id).
     */
    public List<Task> getAllTasksByFilter(TaskFilterForAdmin filter) {
        return taskRepository.findAllByFilter(filter);
    }


    /**
     spare method (from TaskRepository)
     sorting by (Task.userId, Task.id).
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll(Sort.by("user", "id"));
    }


    /**
     spare method (from TaskRepository -> QuerydslPredicateExecutor<Task>)
     filtration; pagination.
     */
    public Page<Task> getTaskPageByFilter(TaskFilterForAdmin filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.user(), task.user.username::containsIgnoreCase)
                .add(filter.dateOfCreation(), task.dateOfCreation::before)
                .buildAnd();
        return taskRepository.findAll(predicate, pageable);
    }

//    public Page<Task> getTaskPageByFilter(TaskFilterForAdmin filter, Pageable pageable) {
//        Predicate predicate = QPredicates.builder()
//                .add(filter.user(), task.user.username::containsIgnoreCase)
//                .add(filter.dateOfCreation(), task.dateOfCreation::before)
//                .buildAnd();
//        Page<Task> taskPage = taskRepository.findAll(predicate, pageable);
//        int page = taskPage.getNumber();
//        int size = taskPage.getSize();
//        long totalElements = taskPage.getTotalElements();
//
//        List<Task> taskList = taskPage.stream()
//                .sorted(Comparator.comparing((Task task) -> task.getUser().getUsername())
//                        .thenComparing(Task::getId)).toList();
//        return new PageImpl<>(taskList, PageRequest.of(page, size), totalElements);
//
//    }


    @Transactional
    public void lockUser(Long id) {
        userRepository.findById(id).get().setIsNonLocked(false);
        log.info("User id={} was locked", id);
    }

    @Transactional
    public void unlockUser(Long id) {
        userRepository.findById(id).get().setIsNonLocked(true);
        log.info("User id={} was unlocked", id);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("User id={} was deleted", id);
    }

}
