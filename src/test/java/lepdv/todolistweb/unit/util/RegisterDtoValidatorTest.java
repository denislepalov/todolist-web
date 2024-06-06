package lepdv.todolistweb.unit.util;

import lepdv.todolistweb.dto.RegisterDto;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.service.UserService;
import lepdv.todolistweb.util.RegisterDtoValidator;
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
class RegisterDtoValidatorTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegisterDtoValidator registerDtoValidator;

    private static final Class<RegisterDto> CLAZZ = RegisterDto.class;
    private static final Class<String> WRONG_CLAZZ = String.class;

    private static final RegisterDto REGISTER_DTO = RegisterDto.builder()
            .username("Ivan")
            .fullName("Ivanov Ivan")
            .password("Ivan")
            .dateOfBirth(LocalDate.of(2000, Month.JANUARY, 1))
            .build();

    public static final User USER = User.builder()
            .username("Ivan")
            .fullName("Ivanov Ivan")
            .password("Ivan")
            .dateOfBirth(LocalDate.of(2000, Month.JANUARY, 1))
            .build();

    @Test
    void supports_shouldGetTrue_whenParameterIsValid() {
        boolean actualResult = registerDtoValidator.supports(CLAZZ);

        assertTrue(actualResult);
    }

    @Test
    void supports_shouldGetFalse_whenParameterIsNotValid() {
        boolean actualResult = registerDtoValidator.supports(WRONG_CLAZZ);

        assertFalse(actualResult);
    }

    @Test
    void validate_shouldValidateRegisterDto() {
        Errors errorsMock = mock(Errors.class);
        doReturn(Optional.of(USER)).when(userService).getByUsername(REGISTER_DTO.getUsername());

        registerDtoValidator.validate(REGISTER_DTO, errorsMock);

        verify(userService).getByUsername(REGISTER_DTO.getUsername());
    }
}