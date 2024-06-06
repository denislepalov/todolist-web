package lepdv.todolistweb.controller;

import jakarta.validation.Valid;
import lepdv.todolistweb.dto.PageResponse;
import lepdv.todolistweb.dto.filter.TaskFilterForAuthUser;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.service.TaskService;
import lepdv.todolistweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static lepdv.todolistweb.util.AuthUser.getAuthUsername;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;


    private Long getAuthUserId() { return userService.getByUsername(getAuthUsername()).get().getId(); }

    @GetMapping("/todo-list")
    public String showTaskPageByAuthUserByFilter(Model model, TaskFilterForAuthUser filter, Pageable pageable) {
        Page<Task> page = taskService.getTaskPageByAuthUserByFilter(filter, pageable);
        model.addAttribute("todoList", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "task/todo_list";
    }


    @GetMapping("/{id}")
    public String showById( @PathVariable("id") Long id, Model model) {
        model.addAttribute("task", taskService.getById(id).get());
        return "task/task_by_id";
    }


    @GetMapping("/new")
    private String createTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "task/new";
    }


    @PostMapping()
    public String createTask( @ModelAttribute("task") @Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "task/new";
        }
        taskService.create(task, getAuthUserId());
        return "redirect:/tasks/todo-list";
    }


    @GetMapping("/{id}/edit")
    public String updateTaskForm( @PathVariable("id") Long id, Model model) {
        model.addAttribute("task", taskService.getById(id).get());
        return "task/edit";
    }


    @PutMapping("/{id}")
    public String updateTask( @ModelAttribute("task") @Valid Task updatingTask, BindingResult bindingResult,
                              @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "task/edit";
        }
        taskService.update(updatingTask, id);
        return "redirect:/tasks/" + id;
    }


    @GetMapping("/{id}/mark-as-completed")
    public String markAsCompleted( @PathVariable("id") Long id) {
        taskService.markAsCompleted(id);
        return "redirect:/tasks/" + id;
    }


    @DeleteMapping("/{id}")
    public String deleteTask( @PathVariable("id") Long id) {
        taskService.delete(id);
        return "redirect:/tasks/todo-list";
    }

}
