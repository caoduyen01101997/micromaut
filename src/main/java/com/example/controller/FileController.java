package com.example.controller;

import com.example.document.FileInfo;
import com.example.service.FileService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

import java.io.IOException;
import java.util.Optional;

@Controller("/files")
@Secured(SecurityRule.IS_AUTHENTICATED)
@ExecuteOn(TaskExecutors.IO)
public class FileController {
    @Inject
    private FileService fileService;

    @Post(consumes = MediaType.MULTIPART_FORM_DATA)
    public HttpResponse<FileInfo> upload(@Part("file") StreamingFileUpload fileUpload) {
        try {
            // Assume userId from security context - simplified, get from Authentication
            Long userId = 1L; // TODO: Get from SecurityContext

            byte[] data = fileUpload.getBytes();
            FileInfo fileInfo = fileService.uploadFile(
                fileUpload.getFilename().orElse("unknown"),
                data,
                fileUpload.getContentType().orElse("application/octet-stream").toString(),
                userId
            );
            return HttpResponse.ok(fileInfo);
        } catch (IOException e) {
            return HttpResponse.serverError("Upload failed");
        }
    }

    @Get("/{id}")
    public HttpResponse<?> download(Long id) {
        Optional<FileInfo> optFile = fileService.findById(id);
        if (optFile.isEmpty()) {
            return HttpResponse.notFound();
        }
        FileInfo fileInfo = optFile.get();
        byte[] data = fileService.downloadFile(fileInfo.getFilePath());
        return HttpResponse.ok(data)
                .contentType(io.micronaut.http.MediaType.of(fileInfo.getContentType()))
                .header("Content-Disposition", "attachment; filename=\"" + fileInfo.getOriginalName() + "\"");
    }

    @Get("/info/{id}")
    public HttpResponse<FileInfo> info(Long id) {
        return fileService.findById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }
}
