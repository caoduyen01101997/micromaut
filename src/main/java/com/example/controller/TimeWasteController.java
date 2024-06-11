package com.example.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/blog")
@ExecuteOn(TaskExecutors.IO)
public class TimeWasteController {
}
