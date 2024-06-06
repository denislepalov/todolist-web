package lepdv.todolistweb.dto.filter;

import java.time.LocalDate;

public record TaskFilterForAuthUser(LocalDate dueDate,
                                    String isCompleted) {
}
