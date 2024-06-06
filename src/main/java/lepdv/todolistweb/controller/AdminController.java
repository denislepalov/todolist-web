package lepdv.todolistweb.controller;

import lepdv.todolistweb.dto.PageResponse;
import lepdv.todolistweb.dto.filter.TaskFilterForAdmin;
import lepdv.todolistweb.dto.filter.UserFilter;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.service.AdminService;
import lepdv.todolistweb.service.TaskService;
import lepdv.todolistweb.service.UserService;
import lepdv.todolistweb.util.Flag;
import lepdv.todolistweb.util.UtilObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static lepdv.todolistweb.util.AuthUser.getAuthUsername;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping()
    public String authorities() {
        return "admin/authorities";
    }


    @GetMapping("/users")
    public String showUserPageByFilter(Model model, UserFilter filter, Pageable pageable) {
        Page<User> page = adminService.getUserPageByFilter(filter, pageable);
        model.addAttribute("users", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "admin/users";
    }


    @GetMapping("/users/{id}")
    public String showUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", adminService.getUserById(id).get());
        UtilObject utilObject = UtilObject.builder()
                .id(id)
                .flag(Flag.ONE)
                .build();
        model.addAttribute("utilObject", utilObject);
        return "admin/user_by_id";
    }


    @GetMapping("/lock-user/{id}")
    private String lockUser(@PathVariable("id") Long id) {
        adminService.lockUser(id);
        return "redirect:/admin/users/" + id;
    }


    @GetMapping("/unlock-user/{id}")
    private String unLockUser(@PathVariable("id") Long id) {
        adminService.unlockUser(id);
        return "redirect:/admin/users/" + id;
    }


    @DeleteMapping("/delete-user")
    public String deleteUser(@ModelAttribute("utilObject") UtilObject utilObject) {

        Flag flag = utilObject.getFlag();
        if (flag.equals(Flag.ONE)) {
            utilObject.setFlag(Flag.TWO);
            return "admin/delete_user";
        }

        if (flag.equals(Flag.TWO)) {
            String adminPasswordFromDB = userService.getByUsername(getAuthUsername()).get().getPassword();

            if (!passwordEncoder.matches(utilObject.getPassword(), adminPasswordFromDB)) {
                return "admin/delete_user";
            }
            utilObject.setFlag(Flag.THREE);
            return "admin/delete_user";
        }

        if (flag.equals(Flag.THREE)) {
            adminService.deleteUser(utilObject.getId());
        }
        return "redirect:/admin/users";
    }


    @GetMapping("/tasks")
    public String showTaskPageByFilter(Model model, TaskFilterForAdmin filter, Pageable pageable) {
        Page<Task> page = adminService.getAllTasksByFilterByPageable(filter, pageable);
        model.addAttribute("tasks", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "admin/tasks";
    }


    @GetMapping("/tasks/{id}")
    public String showTaskById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("task", taskService.getById(id).get());
        return "admin/task_by_id";
    }

}
