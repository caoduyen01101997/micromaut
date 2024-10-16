package com.example.provider;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

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

        // Fetch the user from the database by username
        Optional<User> userOpt = userRepository.findByUsername(username);

        // If user is found, verify password
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return Mono.just(AuthenticationResponse.success(username));
            } else {
                return Mono.just(AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
            }
        } else {
            // User not found, authentication failed
            return Mono.just(AuthenticationResponse.failure(AuthenticationFailureReason.USER_NOT_FOUND));
        }
    }
}
