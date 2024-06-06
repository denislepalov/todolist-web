package lepdv.todolistweb.util;

import lepdv.todolistweb.dto.EditUserDto;
import lepdv.todolistweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ResourceBundle;


@Component
@RequiredArgsConstructor
public class EditUserDtoValidator implements Validator {

    private final UserService userService;


    @Override
    public boolean supports(Class<?> clazz) {
        return EditUserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EditUserDto user = (EditUserDto) target;

        String oldUsername = userService.getById(user.getId()).get().getUsername();
        String newUsername = user.getUsername();

        if (!oldUsername.equals(newUsername) && userService.getByUsername(user.getUsername()).isPresent()) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
            String message = resourceBundle.getString("such.username.already.exist");
            errors.rejectValue("username", "", message);
        }
    }

}
