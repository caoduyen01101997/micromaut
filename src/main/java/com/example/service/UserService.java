package com.example.service;

import com.example.document.User;
import com.example.repository.UserRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {
    @Inject
    private UserRepository userRepository;
    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> list() {
        return userRepository.findAll();
    }

    public User save(User user) {
        boolean isNew = user.getId() == null;
        if (isNew) {
            user.setId(generateId());
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            String pwd = user.getPassword();
            // Nếu chưa phải hash BCrypt thì mới mã hóa
            if (!(pwd.startsWith("$2a") || pwd.startsWith("$2b") || pwd.startsWith("$2y"))) {
                user.setPassword(BCrypt.hashpw(pwd, BCrypt.gensalt(10)));
            }
        }
        return isNew ? userRepository.save(user) : userRepository.update(user);
    }

    public Optional<User> find(@NonNull Long id) {
        return userRepository.findById(id);
    }

    private Long generateId() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Convert the UUID to a string and remove the hyphens
        String uuidStr = uuid.toString().replace("-", "");

        // Convert the first 15 characters of the UUID to a long
        Long id = Long.parseLong(uuidStr.substring(0, 10), 16);

        return id;
    }
}
