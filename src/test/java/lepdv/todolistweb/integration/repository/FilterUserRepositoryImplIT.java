package lepdv.todolistweb.integration.repository;

import lepdv.todolistweb.Constants;
import lepdv.todolistweb.dto.filter.UserFilter;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.integration.IT;
import lepdv.todolistweb.repository.FilterUserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@IT
@RequiredArgsConstructor
@WithMockUser(username = "Admin", authorities = "ADMIN")
class FilterUserRepositoryImplIT /*extends IntegrationTestBase*/ {

    private final FilterUserRepositoryImpl filterUserRepository;



    @Test
    void findAllByFilter_shouldGetFilteredUserList_whenFilterExist() {

        final UserFilter filter = new UserFilter(Constants.USER.getUsername(), Constants.USER.getFullName(),
                LocalDate.of(2000, Month.JANUARY, 2));
        final List<User> expectedResult = List.of(Constants.USER);

        List<User> actualResult = filterUserRepository.findAllByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void findAllByFilter_shouldGetAllUserList_whenFilterNotExist() {
        final UserFilter filter = new UserFilter(null, null, null);

        List<User> actualResult = filterUserRepository.findAllByFilter(filter);

        Assertions.assertFalse(actualResult.isEmpty());
        Assertions.assertEquals(Constants.USER_LIST, actualResult);
    }

    @Test
    void findAllByFilter_shouldGetEmptyUserList_whenFilterDoNotHaveMatches() {

        final UserFilter filter = new UserFilter("dummy", "dummy",
                LocalDate.of(9999, Month.JANUARY, 2));

        List<User> actualResult = filterUserRepository.findAllByFilter(filter);

        Assertions.assertTrue(actualResult.isEmpty());
    }

}