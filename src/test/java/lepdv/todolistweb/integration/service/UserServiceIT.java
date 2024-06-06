package lepdv.todolistweb.integration.service;

import lepdv.todolistweb.Constants;
import lepdv.todolistweb.entity.Role;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.integration.IT;
import lepdv.todolistweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;


@IT
@RequiredArgsConstructor
class UserServiceIT /*extends IntegrationTestBase*/ {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    @Test
    void loadUserByUsername_shouldGetUserDetails_whenExist() {
        UserDetails actualResult = userService.loadUserByUsername(Constants.USER.getUsername());

        Assertions.assertEquals(Constants.USER.getUsername(), actualResult.getUsername());
        Assertions.assertEquals(Constants.USER.getPassword(), actualResult.getPassword());
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenNotExist() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("dummy"));
    }


    @Test
    void getById_shouldGetOptionalOfUser_whenExist() {
        Optional<User> actualResult = userService.getById(Constants.USER.getId());

        Assertions.assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> Assertions.assertEquals(Constants.USER, actual));
    }

    @Test
    void getById_shouldGetEmptyOptional_whenNotExist() {
        Optional<User> actualResult = userService.getById(999L);

        Assertions.assertTrue(actualResult.isEmpty());
    }


    @Test
    void getAvatar_shouldGetOptionalOfByteArray_whenExist() {
        Optional<byte[]> actualResult = userService.getAvatar(Constants.USER.getId());

        Assertions.assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> Assertions.assertArrayEquals(Constants.BYTE_ARRAY, actual));
    }

    @Test
    void getAvatar_shouldGetEmptyOptional_whenNotExist() {
        Optional<byte[]> actualResult = userService.getAvatar(999L);

        Assertions.assertTrue(actualResult.isEmpty());
    }


    @Test
    void getByUsername_shouldGetOptionalOfUser_whenExist() {
        Optional<User> actualResult = userService.getByUsername(Constants.USER.getUsername());

        Assertions.assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> Assertions.assertEquals(Constants.USER, actual));
    }

    @Test
    void getByUsername_shouldGetEmptyOptional_whenNotExist() {
        Optional<User> actualResult = userService.getByUsername("dummy");

        Assertions.assertTrue(actualResult.isEmpty());
    }

    @Test
    void register_shouldRegisterNewUser() {
        User expectedUser = User.builder()
                .id(4L)
                .username("Petr")
                .password("$2a$10$mHGVGHG5z3AWrbrF.T.5ce.hcwp4efQgAVWKKkFSMqmKXEZKmsIpO")
                .fullName("Petrov Petr")
                .dateOfBirth(LocalDate.of(1980, Month.AUGUST, 15))
                .role(Role.USER)
                .isNonLocked(true)
                .image("lock.png")
                .build();

        userService.register(Constants.REGISTER_DTO);
        Optional<User> newUser = userService.getByUsername(Constants.REGISTER_DTO.getUsername());

        Assertions.assertTrue(newUser.isPresent());
        newUser.ifPresent(actual -> Assertions.assertAll(
                () -> Assertions.assertEquals(expectedUser.getId(), actual.getId()),
                () -> Assertions.assertEquals(expectedUser.getUsername(), actual.getUsername()),
                () -> Assertions.assertEquals(expectedUser.getFullName(), actual.getFullName()),
                () -> Assertions.assertEquals(expectedUser.getDateOfBirth(), actual.getDateOfBirth()),
                () -> Assertions.assertEquals(expectedUser.getRole(), actual.getRole()),
                () -> Assertions.assertEquals(expectedUser.getIsNonLocked(), actual.getIsNonLocked()),
                () -> Assertions.assertEquals(expectedUser.getImage(), actual.getImage())
        ));
    }

    @Test
    void update_shouldUpdateUser() {
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

        userService.update(Constants.EDIT_USER_DTO);
        Optional<User> updatedUser = userService.getByUsername(Constants.EDIT_USER_DTO.getUsername());

        Assertions.assertTrue(updatedUser.isPresent());
        updatedUser.ifPresent(actual -> Assertions.assertEquals(expectedUser, actual));
    }

    @Test
    void changePassword_shouldChangeUserPassword() {
        userService.editPassword(Constants.USER.getId(), Constants.TEST_PASSWORD);
        Optional<User> optionalUser = userService.getById(Constants.USER.getId());

        optionalUser.ifPresent(user -> Assertions.assertTrue(passwordEncoder.matches(Constants.TEST_PASSWORD, user.getPassword())));
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        userService.deleteUser(Constants.USER.getId());
        Optional<User> deletedUser = userService.getById(Constants.USER.getId());

        Assertions.assertTrue(deletedUser.isEmpty());
    }
}