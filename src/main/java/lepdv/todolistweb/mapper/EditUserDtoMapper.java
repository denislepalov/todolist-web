package lepdv.todolistweb.mapper;

import lepdv.todolistweb.dto.EditUserDto;
import lepdv.todolistweb.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
public class EditUserDtoMapper {

   public User map(EditUserDto object) {
        User user = new User();
        user.setId(object.getId());
        user.setUsername(object.getUsername());
        user.setFullName(object.getFullName());
        user.setDateOfBirth(object.getDateOfBirth());

        Optional.ofNullable(object.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> user.setImage(image.getOriginalFilename()));
        return user;
    }
}
