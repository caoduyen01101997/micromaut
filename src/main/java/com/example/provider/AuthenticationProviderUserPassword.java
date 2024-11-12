package com.example.provider;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.reactivestreams.Publisher;

import com.example.document.User;
import com.example.repository.UserRepository;

import reactor.core.publisher.Mono;


@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
    
    @Inject
    private UserRepository userRepository;
    
    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        String username = authenticationRequest.getIdentity().toString();
        String password = authenticationRequest.getSecret().toString();

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", user.getId());
                claims.put("role", user.getRole());

                // Use AuthenticationResponse.success to add claims directly
                return Mono.just(AuthenticationResponse.success(username, claims));
            } else {
                return Mono.just(AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
            }
        } else {
            return Mono.just(AuthenticationResponse.failure(AuthenticationFailureReason.USER_NOT_FOUND));
        }
    }
}
