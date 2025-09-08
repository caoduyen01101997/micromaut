package com.example.provider;

import com.example.document.User;
import com.example.repository.UserRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import jakarta.inject.Singleton;
import org.mindrot.jbcrypt.BCrypt;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Authentication provider duy nhất sử dụng BCrypt để kiểm tra mật khẩu đã hash.
 * Loại bỏ so sánh plain-text và tránh trùng lặp với phiên bản cũ.
 */
@Singleton
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    private final UserRepository userRepository;

    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        String username = Objects.toString(authenticationRequest.getIdentity(), null);
        String rawPassword = Objects.toString(authenticationRequest.getSecret(), null);
        if (username == null || rawPassword == null) {
            return Mono.just(AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            LOG.debug("Login fail - user not found: {}", username);
            return Mono.just(AuthenticationResponse.failure(AuthenticationFailureReason.USER_NOT_FOUND));
        }
        User user = userOpt.get();
        String stored = user.getPassword();
        if (stored == null || stored.isBlank()) {
            LOG.debug("Login fail - empty password for user: {}", username);
            return Mono.just(AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
        }

        if (stored.startsWith("$2")) { // BCrypt hash path
            boolean ok = false;
            try {
                ok = BCrypt.checkpw(rawPassword, stored);
            } catch (IllegalArgumentException e) {
                LOG.warn("Invalid BCrypt hash format for user {}", username);
            }
            if (ok) {
                return Mono.just(successResponse(user));
            } else {
                LOG.debug("Login fail - hash mismatch for user: {}", username);
                return Mono.just(AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
            }
        } else {
            // Legacy plain password detected -> migrate automatically
            if (stored.equals(rawPassword)) {
                String newHash = BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
                user.setPassword(newHash);
                try {
                    userRepository.update(user);
                    LOG.info("Migrated plain password to BCrypt for user: {}", username);
                } catch (Exception ex) {
                    LOG.error("Failed to update migrated password for user {}: {}", username, ex.getMessage());
                }
                return Mono.just(successResponse(user));
            } else {
                LOG.debug("Login fail - legacy plain password mismatch for user: {}", username);
                return Mono.just(AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
            }
        }
    }

    private AuthenticationResponse successResponse(User user) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", user.getId());
        attributes.put("role", user.getRole());
        attributes.put("roles", Collections.singletonList(user.getRole()));
        return AuthenticationResponse.success(user.getUsername(), attributes);
    }
}