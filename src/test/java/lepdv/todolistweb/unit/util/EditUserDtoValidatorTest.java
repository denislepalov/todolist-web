package lepdv.todolistweb.unit.util;

import lepdv.todolistweb.dto.EditUserDto;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.service.UserService;
import lepdv.todolistweb.util.EditUserDtoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditUserDtoValidatorTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private EditUserDtoValidator editUserDtoValidator;

    private static final Class<EditUserDto> clazz = EditUserDto.class;
    private static final Class<String> wrongClazz = String.class;

    private static final EditUserDto EDIT_USER_DTO = EditUserDto.builder()
            .id(2L)
            .username("Ivan")
            .fullName("Ivanov Ivan")
            .dateOfBirth(LocalDate.of(2000, Month.JANUARY, 1))
            .build();

    public static final User USER = User.builder()
            .id(3L)
            .username("Katya")
            .fullName("Petrova Katya")
            .dateOfBirth(LocalDate.of(2000, Month.JANUARY, 1))
            .build();


    @Test
    void supports_shouldGetTrue_whenParameterIsValid() {
        boolean actualResult = editUserDtoValidator.supports(clazz);

        assertTrue(actualResult);
    }

    @Test
    void supports_shouldGetFalse_whenParameterIsNotValid() {
        boolean actualResult = editUserDtoValidator.supports(wrongClazz);

        assertFalse(actualResult);
    }

    @Test
    void validate_shouldValidateEditUserDto() {
        Errors errorsMock = mock(Errors.class);
        doReturn(Optional.of(USER)).when(userService).getById(EDIT_USER_DTO.getId());
        doReturn(Optional.of(USER)).when(userService).getByUsername(EDIT_USER_DTO.getUsername());

        editUserDtoValidator.validate(EDIT_USER_DTO, errorsMock);

        verify(userService).getById(EDIT_USER_DTO.getId());
        verify(userService).getByUsername(EDIT_USER_DTO.getUsername());
    }
}