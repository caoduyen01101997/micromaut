package com.example.service;

import com.example.DTO.CustomUserDetails;
import com.example.document.User;
import com.example.repository.UserRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
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
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        String username = authenticationRequest.getIdentity().toString();
        String password = authenticationRequest.getSecret().toString();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return Mono.just((AuthenticationResponse) new CustomUserDetails(username, Collections.singletonList(user.get().getRole())));        }
        return Mono.just(new AuthenticationFailed());
    }
}