package com.example.provider;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;


@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
//    @Override
//    public AuthenticationResponse  authenticate(@Nullable HttpRequest httpRequest,
//                                                          @NonNull AuthenticationRequest<String, String> authenticationRequest) {
//        return authenticationRequest.getIdentity().equals("sherlock") && authenticationRequest.getSecret().equals("password")
//                ? AuthenticationResponse.success(authenticationRequest.getIdentity())
//                : AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
//
//    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        if (authenticationRequest.getIdentity().equals("sherlock") && authenticationRequest.getSecret().equals("password")) {
            return Mono.just(AuthenticationResponse.success(authenticationRequest.getIdentity().toString()));
        } else {
            return Mono.just(AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
        }
    }
}
