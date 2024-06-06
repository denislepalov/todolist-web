package lepdv.todolistweb.repository;

import lepdv.todolistweb.dto.filter.TaskFilterForAdmin;
import lepdv.todolistweb.dto.filter.TaskFilterForAuthUser;
import lepdv.todolistweb.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FilterTaskRepository {

    public List<Task> findAllByFilter(TaskFilterForAdmin filter);

    public Page<Task> findAllByFilterByPageable(TaskFilterForAdmin filter, Pageable pageable);

    public List<Task> findAllByAuthUserByFilter(TaskFilterForAuthUser filter2);
}
