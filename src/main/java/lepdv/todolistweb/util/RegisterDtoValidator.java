package lepdv.todolistweb.util;

import lepdv.todolistweb.dto.RegisterDto;
import lepdv.todolistweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class RegisterDtoValidator implements Validator {

    private final UserService userService;


    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterDto user = (RegisterDto) target;

        if (userService.getByUsername(user.getUsername()).isPresent()) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
            String message = resourceBundle.getString("such.username.already.exist");
            errors.rejectValue("username", "", message);
        }
    }
}


