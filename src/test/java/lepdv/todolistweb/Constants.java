package lepdv.todolistweb;

import lepdv.todolistweb.dto.EditUserDto;
import lepdv.todolistweb.dto.RegisterDto;
import lepdv.todolistweb.entity.Role;
import lepdv.todolistweb.entity.Task;
import lepdv.todolistweb.entity.User;
import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@UtilityClass
public class Constants {

    public static final String TEST_PASSWORD = "test password";

    public static final User USER = User.builder()
            .id(2L)
            .username("Ivan")
            .password("$2a$10$JfoL9fN.fl4DtP.mUQAF0..OzWxIE2ffAq7nWY4XtXKazpYCd5HSK")
            .fullName("Ivanov Ivan")
            .dateOfBirth(LocalDate.of(2000, Month.JANUARY, 1))
            .role(Role.USER)
            .isNonLocked(true)
            .image("lock.png")
            .build();
    public static final User ADMIN = User.builder()
            .id(1L)
            .username("Admin")
            .password("$2a$10$QqbD8Up32CATm2DSVVjIDea08KuxC/RL9.9SFVcMP6FW5nHGl5PIG")
            .fullName("Admin")
            .dateOfBirth(LocalDate.of(1990, Month.JANUARY, 1))
            .role(Role.ADMIN)
            .isNonLocked(true)
            .image(null)
            .build();
    public static final User KATYA = User.builder()
            .id(3L)
            .username("Katya")
            .password("$2a$10$f0A/1pjXviu82xuuG5AKreDlb0tiAoWzBMnbphJz1oPNkzaZ2omRe")
            .fullName("Petrova Katya")
            .dateOfBirth(LocalDate.of(2010, Month.JANUARY, 1))
            .role(Role.USER)
            .isNonLocked(true)
            .image(null)
            .build();

    public static final List<User> USER_LIST = List.of(ADMIN, USER, KATYA);


    public static final Task TASK = Task.builder()
            .id(1L)
            .description("Ivan task1")
            .dateOfCreation(LocalDate.of(2023, Month.MAY, 1))
            .dueDate(LocalDate.of(2025, Month.MAY, 11))
            .isCompleted("Not completed")
            .user(USER)
            .build();
    public static final Task TASK_2 = Task.builder()
            .id(2L)
            .description("Ivan task2")
            .dateOfCreation(LocalDate.of(2023, Month.MAY, 10))
            .dueDate(LocalDate.of(2025, Month.MAY, 20))
            .isCompleted("Not completed")
            .user(USER)
            .build();
    public static final Task TASK_3 = Task.builder()
            .id(3L)
            .description("Ivan task3")
            .dateOfCreation(LocalDate.of(2023, Month.MAY, 20))
            .dueDate(LocalDate.of(2025, Month.MAY, 30))
            .isCompleted("Not completed")
            .user(USER)
            .build();
    public static final Task TASK_4 = Task.builder()
            .id(4L)
            .description("Katya task1")
            .dateOfCreation(LocalDate.of(2023, Month.JULY, 1))
            .dueDate(LocalDate.of(2025, Month.MAY, 11))
            .isCompleted("Not completed")
            .user(KATYA)
            .build();
    public static final Task TASK_5 = Task.builder()
            .id(5L)
            .description("Katya task2")
            .dateOfCreation(LocalDate.of(2023, Month.JULY, 10))
            .dueDate(LocalDate.of(2025, Month.MAY, 20))
            .isCompleted("Not completed")
            .user(KATYA)
            .build();
    public static final Task TASK_6 = Task.builder()
            .id(6L)
            .description("Katya task3")
            .dateOfCreation(LocalDate.of(2023, Month.JULY, 20))
            .dueDate(LocalDate.of(2025, Month.MAY, 30))
            .isCompleted("Not completed")
            .user(KATYA)
            .build();

    public static final List<Task> TASK_LIST = List.of(TASK, TASK_2, TASK_3, TASK_4, TASK_5, TASK_6);


    public static final byte[] BYTE_ARRAY;
    static {
        try {
            BYTE_ARRAY = Files.readAllBytes(Path.of("src", "test", "resources", "static", "lock.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final RegisterDto REGISTER_DTO = RegisterDto.builder()
            .username("Petr")
            .fullName("Petrov Petr")
            .password("Petr")
            .dateOfBirth(LocalDate.of(1980, Month.AUGUST, 15))
            .image(new MockMultipartFile("image", "lock.png", "multipart/form-data", BYTE_ARRAY))
            .build();

    public static final EditUserDto EDIT_USER_DTO = EditUserDto.builder()
            .id(2L)
            .username("Ivan")
            .fullName("Test Test")
            .dateOfBirth(LocalDate.of(2000, Month.JANUARY, 1))
            .image(new MockMultipartFile("lock.png", "lock.png", "", BYTE_ARRAY))
            .build();
}
