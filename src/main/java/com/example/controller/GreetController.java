package com.example.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
@Controller("/greet")
public class GreetController {
    @Get("/{name}")
    public String greet(String name) {
        return  name;
    }
    
}
