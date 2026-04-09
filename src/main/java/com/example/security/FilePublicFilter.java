package com.example.security;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

@Filter("/files/**")
@Singleton
public class FilePublicFilter implements HttpServerFilter {

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        // Skip all security processing for /files paths - direct to controller
        return chain.proceed(request);
    }
}
