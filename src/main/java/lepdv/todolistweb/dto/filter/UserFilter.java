package lepdv.todolistweb.dto.filter;

import java.time.LocalDate;


public record UserFilter(String username,
                         String fullName,
                         LocalDate dateOfBirth) {
}
