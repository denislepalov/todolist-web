package lepdv.todolistweb.controller;

import jakarta.validation.Valid;
import lepdv.todolistweb.dto.EditUserDto;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.service.UserService;
import lepdv.todolistweb.util.EditUserDtoValidator;
import lepdv.todolistweb.util.Flag;
import lepdv.todolistweb.util.UtilObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static lepdv.todolistweb.util.AuthUser.getAuthUsername;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EditUserDtoValidator editUserDtoValidator;
    private final PasswordEncoder passwordEncoder;


    private Long getAuthUserId() {
        return userService.getByUsername(getAuthUsername()).get().getId();
    }

    @GetMapping()
    public String showUser(Model model) {
        User user = userService.getById(getAuthUserId()).get();
        model.addAttribute("user", user);
        UtilObject utilObject = UtilObject.builder()
                .id(getAuthUserId())
                .flag(Flag.ONE)
                .build();
        model.addAttribute("utilObject", utilObject);
        return "user/user";
    }

    @GetMapping("/{id}/avatar")
    @ResponseBody
    public byte[] findAvatar(@PathVariable("id") Long id) {
        return userService.getAvatar(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/edit")
    public String editUserForm(Model model) {
        User user = userService.getById(getAuthUserId()).get();
        model.addAttribute("user", user);
        return "user/edit";
    }


    @PutMapping()
    public String update( @ModelAttribute("user") @Valid EditUserDto editUserDto, BindingResult bindingResult) {
        editUserDtoValidator.validate(editUserDto, bindingResult);
        if (bindingResult.hasErrors())
            return "user/edit";
        userService.update(editUserDto);
        return "redirect:/user";
    }


    @PutMapping("/edit-password")
    public String editPassword( @ModelAttribute("utilObject") @Valid UtilObject utilObject,
                                 BindingResult bindingResult) {
        Flag flag = utilObject.getFlag();
        if (flag.equals(Flag.ONE)) {
            utilObject.setFlag(Flag.TWO);
            return "user/edit_password";
        }
        if (flag.equals(Flag.TWO)) {
            String passwordFromDB = userService.getById(utilObject.getId()).get().getPassword();
            if (!passwordEncoder.matches(utilObject.getPassword(), passwordFromDB))
                return "user/edit_password";
            utilObject.setFlag(Flag.THREE);
            return "user/edit_password";
        }
        if (flag.equals(Flag.THREE)) {
            if (bindingResult.hasFieldErrors("password")) {
                return "user/edit_password";
            }
            userService.editPassword(utilObject.getId(), utilObject.getPassword());
            utilObject.setFlag(Flag.FOUR);
        }
        return "user/edit_password";
    }


    @DeleteMapping("/delete-account")
    public String deleteAccount(@ModelAttribute("utilObject") UtilObject utilObject) {

        Flag flag = utilObject.getFlag();
        if (flag.equals(Flag.ONE)) {
            utilObject.setFlag(Flag.TWO);
            return "user/delete_account";
        }

        if (flag.equals(Flag.TWO)) {
            String passwordFromDB = userService.getById(utilObject.getId()).get().getPassword();
            if (!passwordEncoder.matches(utilObject.getPassword(), passwordFromDB)) {
                return "user/delete_account";
            }
            utilObject.setFlag(Flag.THREE);
            return "user/delete_account";
        }

        if (flag.equals(Flag.THREE)) {
            userService.deleteUser(utilObject.getId());
        }
        return "redirect:/authenticate/login";
    }


}
