package com.example.service;

import com.example.DTO.CustomUserDetails;
import com.example.document.User;
import com.example.repository.UserRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.mindrot.jbcrypt.BCrypt;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.Optional;

@Singleton
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Publisher authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        String username = authenticationRequest.getIdentity().toString();
        String password = authenticationRequest.getSecret().toString();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return Mono.just(new AuthenticationFailed());
        }
        User u = userOpt.get();
        String stored = u.getPassword();
        if (stored != null && stored.startsWith("$2")) { // đã hash BCrypt
            if (BCrypt.checkpw(password, stored)) {
                return Mono.just(new CustomUserDetails(username, Collections.singletonList(u.getRole())));
            }
        } else {
            // Trường hợp dữ liệu cũ còn plain (không tự migrate nữa) => fail đăng nhập để buộc admin hash lại.
        }
        return Mono.just(new AuthenticationFailed());
    }
}