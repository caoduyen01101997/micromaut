package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.MediaType;
@Controller("/hello-world")
public class helloController {
    @Get // <2>
    @Produces(MediaType.TEXT_PLAIN) // <3>
    public String index() {
        return "Hello World"; // <4>
    }
}
