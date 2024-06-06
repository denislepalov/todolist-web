package lepdv.todolistweb.mapper;

import lepdv.todolistweb.dto.RegisterDto;
import lepdv.todolistweb.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
public class RegisterDtoMapper {

    public User map(RegisterDto object) {
        User user = new User();
        user.setUsername(object.getUsername());
        user.setFullName(object.getFullName());
        user.setPassword(object.getPassword());
        user.setDateOfBirth(object.getDateOfBirth());

        Optional.ofNullable(object.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> user.setImage(image.getOriginalFilename()));
        return user;
    }

}
