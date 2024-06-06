package lepdv.todolistweb.integration.util;

import lepdv.todolistweb.Constants;
import lepdv.todolistweb.util.AuthUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;


@ActiveProfiles("test")
@WithMockUser(username = "Ivan", authorities = "USER")
@SpringBootTest()
class AuthUserIT {



    @Test
    void test_cannot_instantiate() {
        Assertions.assertThrows(InvocationTargetException.class, () -> {
            Constructor<AuthUser> constructor = AuthUser.class.getDeclaredConstructor();
            Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }



    @Test
    void getAuthUsername_shouldGetUsername() {
        final String expectedResult = Constants.USER.getUsername();

        String actualResult = AuthUser.getAuthUsername();

        Assertions.assertEquals(expectedResult, actualResult);
    }


}