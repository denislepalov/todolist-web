package lepdv.todolistweb.service;

import lepdv.todolistweb.dto.EditUserDto;
import lepdv.todolistweb.dto.RegisterDto;
import lepdv.todolistweb.entity.Role;
import lepdv.todolistweb.entity.User;
import lepdv.todolistweb.mapper.EditUserDtoMapper;
import lepdv.todolistweb.mapper.RegisterDtoMapper;
import lepdv.todolistweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterDtoMapper registerDtoMapper;
    private final EditUserDtoMapper editUserDtoMapper;
    private final ImageService imageService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }



    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<byte[]> getAvatar(Long id) {
        return userRepository.findById(id)
                .map(User::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Transactional
    public void register(RegisterDto registerDto) {
        upload(registerDto.getImage());
        User user = registerDtoMapper.map(registerDto);
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setRole(Role.USER);
        user.setIsNonLocked(true);
        User savedUser = userRepository.save(user);
        log.info("New user was registered id={}, username={}", savedUser.getId(), savedUser.getUsername());
    }

    @SneakyThrows
    private void upload(MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    @Transactional
    public void update(EditUserDto editUserDto) {
        upload(editUserDto.getImage());
        User user = editUserDtoMapper.map(editUserDto);
        User userFromDB = userRepository.findById(user.getId()).get();

        userFromDB.setUsername(user.getUsername());
        userFromDB.setFullName(user.getFullName());
        userFromDB.setDateOfBirth(user.getDateOfBirth());
        Optional.ofNullable(user.getImage()).ifPresent(userFromDB::setImage);
        log.info("User id={} was updated", user.getId());
    }

    @Transactional
    public void editPassword(Long id, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = userRepository.findById(id).get();
        user.setPassword(encodedPassword);
        log.info("User id={} password was edited", user.getId());
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("User id={} was deleted", id);
    }

}
