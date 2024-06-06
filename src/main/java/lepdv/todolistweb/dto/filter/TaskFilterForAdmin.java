package lepdv.todolistweb.dto.filter;

import java.time.LocalDate;

public record TaskFilterForAdmin(String user,
                                 LocalDate dateOfCreation) {
}
