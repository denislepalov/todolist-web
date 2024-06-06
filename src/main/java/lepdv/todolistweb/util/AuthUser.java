package lepdv.todolistweb.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Object, that used for getting authenticated username.
 */

@UtilityClass
public class AuthUser {

    public static String getAuthUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? ""  : authentication.getName();
    }
}
