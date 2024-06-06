package lepdv.todolistweb.unit.mapper;

import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.mapper.RegisterDtoMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static lepdv.todolistweb.Constants.REGISTER_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RegisterDtoMapperTest {

    private final RegisterDtoMapper registerDtoMapper = new RegisterDtoMapper();

    public static final User USER = User.builder()
            .username("Petr")
            .fullName("Petrov Petr")
            .password("Petr")
            .dateOfBirth(LocalDate.of(1980, Month.AUGUST, 15))
            .image("lock.png")
            .build();


    @Test
    void map_shouldMapRegisterDtoToUser() {
        User actualResult = registerDtoMapper.map(REGISTER_DTO);

        assertEquals(USER, actualResult);
    }
}