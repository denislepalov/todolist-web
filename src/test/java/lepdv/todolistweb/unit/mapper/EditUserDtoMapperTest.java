package lepdv.todolistweb.unit.mapper;

import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.mapper.EditUserDtoMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static lepdv.todolistweb.Constants.EDIT_USER_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;


class EditUserDtoMapperTest {

    private final EditUserDtoMapper editUserDtoMapper = new EditUserDtoMapper();

    private static final User USER = User.builder()
            .id(2L)
            .username("Ivan")
            .fullName("Test Test")
            .dateOfBirth(LocalDate.of(2000, Month.JANUARY, 1))
            .image("lock.png")
            .build();


    @Test
    void map_shouldMapEditUserDtoToUser() {
        User actualResult = editUserDtoMapper.map(EDIT_USER_DTO);

        assertEquals(USER, actualResult);
    }
}