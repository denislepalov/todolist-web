package lepdv.todolistweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
public class RegisterDto {


    @NotBlank(message = "{username.can.not.be.empty}")
    @Size(min = 3, max = 100, message = "{username.should.be.from.3.to.100.symbols}")
    private String username;

    @Size(max = 100, message = "{full.name.can.not.be.more.than.100.symbols}")
    private String fullName;

    @NotBlank(message = "{password.can.not.be.empty}")
    @Size(min = 3, max = 100, message = "{password.should.be.from.3.to.100.symbols}")
    private String password;

    @PastOrPresent(message = "{date.of.birth.can.not.be.in.future}")
    private LocalDate dateOfBirth;

    private MultipartFile image;
}
