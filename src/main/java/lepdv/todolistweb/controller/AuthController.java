package lepdv.todolistweb.controller;

import jakarta.validation.Valid;
import lepdv.todolistweb.dto.RegisterDto;
import lepdv.todolistweb.service.UserService;
import lepdv.todolistweb.util.RegisterDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RegisterDtoValidator registerDtoValidator;



    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }


    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new RegisterDto());
        return "auth/registration";
    }


    @PostMapping("/registration")
    public String register(@ModelAttribute("user") @Valid RegisterDto registerDto, BindingResult bindingResult) {

        registerDtoValidator.validate(registerDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        userService.register(registerDto);
        return "redirect:/logout";
    }




}
