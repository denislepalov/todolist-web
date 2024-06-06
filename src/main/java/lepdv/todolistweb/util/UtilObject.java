package lepdv.todolistweb.util;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * Object, that used for delete account and change password.
 */

@Data
@AllArgsConstructor
@Builder
@FieldNameConstants
public class UtilObject {

    private Long id;

    @NotBlank(message = "{notBlank.error.message.password}")
    @Size(min = 3, max = 100, message = "{size.error.message.password}")
    private String password;

    private Flag flag;

}
