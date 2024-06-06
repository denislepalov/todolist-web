package lepdv.todolistweb.unit.service;

import lepdv.todolistweb.entity.Role;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.mapper.EditUserDtoMapper;
import lepdv.todolistweb.mapper.RegisterDtoMapper;
import lepdv.todolistweb.repository.UserRepository;
import lepdv.todolistweb.service.ImageService;
import lepdv.todolistweb.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static lepdv.todolistweb.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RegisterDtoMapper registerDtoMapper;
    @Mock
    private EditUserDtoMapper editUserDtoMapper;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private UserService userService;


    @Test
    void loadUserByUsername_shouldGetUserDetails_whenExist() {
        doReturn(Optional.of(USER)).when(userRepository).findByUsername(USER.getUsername());


        UserDetails actualResult = userService.loadUserByUsername(USER.getUsername());

        verify(userRepository).findByUsername(USER.getUsername());
        assertEquals(USER.getUsername(), actualResult.getUsername());
        assertEquals(USER.getPassword(), actualResult.getPassword());
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenNotExist() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("dummy"));
        verify(userRepository).findByUsername("dummy");
    }


    @Test
    void getById_shouldGetOptionalOfUser_whenExist() {
        doReturn(Optional.of(USER)).when(userRepository).findById(USER.getId());

        Optional<User> actualResult = userService.getById(USER.getId());

        verify(userRepository).findById(USER.getId());
        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> assertEquals(USER, actual));
    }

    @Test
    void getById_shouldGetEmptyOptional_whenNotExist() {
        Optional<User> actualResult = userService.getById(999L);

        assertTrue(actualResult.isEmpty());
    }


    @Test
    void getAvatar_shouldGetOptionalOfByteArray_whenExist() {
        doReturn(Optional.of(USER)).when(userRepository).findById(USER.getId());
        doReturn(Optional.of(BYTE_ARRAY)).when(imageService).get(USER.getImage());

        Optional<byte[]> actualResult = userService.getAvatar(USER.getId());

        verify(userRepository).findById(USER.getId());
        verify(imageService).get(USER.getImage());
        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> assertArrayEquals(BYTE_ARRAY, actual));
    }

    @Test
    void getAvatar_shouldGetEmptyOptional_whenNotExist() {
        Optional<byte[]> actualResult = userService.getAvatar(999L);

        assertTrue(actualResult.isEmpty());
    }


    @Test
    void getByUsername_shouldGetOptionalOfUser_whenExist() {
        doReturn(Optional.of(USER)).when(userRepository).findByUsername(USER.getUsername());

        Optional<User> actualResult = userService.getByUsername(USER.getUsername());

        verify(userRepository).findByUsername(USER.getUsername());
        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> assertEquals(USER, actual));
    }

    @Test
    void getByUsername_shouldGetEmptyOptional_whenNotExist() {
        Optional<User> actualResult = userService.getByUsername("dummy");

        assertTrue(actualResult.isEmpty());
    }

    @Test
    void register_shouldRegisterNewUser() {
        final User savingUser = User.builder()
                .username("Petr")
                .fullName("Petrov Petr")
                .password("Petr")
                .dateOfBirth(LocalDate.of(1980, Month.AUGUST, 15))
                .image("lock.png")
                .build();
        final User expectedUser = User.builder()
                .id(4L)
                .username("Petr")
                .password("$2a$10$mHGVGHG5z3AWrbrF.T.5ce.hcwp4efQgAVWKKkFSMqmKXEZKmsIpO")
                .fullName("Petrov Petr")
                .dateOfBirth(LocalDate.of(1980, Month.AUGUST, 15))
                .role(Role.USER)
                .isNonLocked(true)
                .image("lock.png")
                .build();
        doReturn(savingUser).when(registerDtoMapper).map(REGISTER_DTO);
        doReturn(expectedUser.getPassword()).when(passwordEncoder).encode(savingUser.getPassword());
        doReturn(expectedUser).when(userRepository).save(savingUser);

        userService.register(REGISTER_DTO);

        verify(registerDtoMapper).map(REGISTER_DTO);
        verify(passwordEncoder).encode("Petr");
        verify(userRepository).save(savingUser);
    }

    @Test
    void update_shouldUpdateUser() {
        final User updatingUser = User.builder()
                .id(2L)
                .username("Ivan")
                .fullName("Test Test")
                .dateOfBirth(LocalDate.of(2000, Month.JANUARY, 1))
                .image("lock.png")
                .build();
        final User expectedUser = User.builder()
                .id(2L)
                .username("Ivan")
                .password("$2a$10$JfoL9fN.fl4DtP.mUQAF0..OzWxIE2ffAq7nWY4XtXKazpYCd5HSK")
                .fullName("Test Test")
                .dateOfBirth(LocalDate.of(2000, Month.JANUARY, 1))
                .role(Role.USER)
                .isNonLocked(true)
                .image("lock.png")
                .build();
        doReturn(updatingUser).when(editUserDtoMapper).map(EDIT_USER_DTO);
        doReturn(Optional.of(expectedUser)).when(userRepository).findById(updatingUser.getId());

        userService.update(EDIT_USER_DTO);

        verify(editUserDtoMapper).map(EDIT_USER_DTO);
        verify(userRepository).findById(updatingUser.getId());
    }

    @Test
    void changePassword_shouldChangeUserPassword() {
        doReturn(USER.getPassword()).when(passwordEncoder).encode(USER.getPassword());
        doReturn(Optional.of(USER)).when(userRepository).findById(USER.getId());

        userService.editPassword(USER.getId(), USER.getPassword());

        verify(passwordEncoder).encode(USER.getPassword());
        verify(userRepository).findById(USER.getId());
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        doNothing().when(userRepository).deleteById(USER.getId());

        userService.deleteUser(USER.getId());

        verify(userRepository).deleteById(USER.getId());
    }
}