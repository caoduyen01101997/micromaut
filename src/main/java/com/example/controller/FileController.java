package com.example.controller;

import com.example.document.FileInfo;
import com.example.service.FileService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller("/files")
@Secured(SecurityRule.IS_ANONYMOUS)
@ExecuteOn(TaskExecutors.IO)
public class FileController {
    @Inject
    private FileService fileService;

    // CREATE - Upload file
    @Post(consumes = MediaType.MULTIPART_FORM_DATA)
    public MutableHttpResponse<FileInfo> upload(
            @Part("file") CompletedFileUpload fileUpload,
            @Part("name") String name) {
        try {
            Long userId = 1L; // TODO: Get from SecurityContext

            FileInfo fileInfo = fileService.uploadFile(
                fileUpload.getFilename(),
                name,
                fileUpload.getInputStream(),
                fileUpload.getSize(),
                fileUpload.getContentType().orElse(MediaType.of("application/octet-stream")).toString(),
                userId
            );
            return HttpResponse.ok(fileInfo);
        } catch (IOException e) {
            return HttpResponse.serverError(new FileInfo());
        }
    }

    // READ - View file inline (for <img src>, gallery)
    @Get("/view/{id}")
    public HttpResponse<?> view(Long id) {
        Optional<FileInfo> optFile = fileService.findById(id);
        if (optFile.isEmpty()) {
            return HttpResponse.notFound();
        }
        FileInfo fileInfo = optFile.get();
        InputStream dataStream = fileService.downloadFileStream(fileInfo.getFilePath());
        return HttpResponse.ok(new StreamedFile(dataStream, MediaType.of(fileInfo.getContentType()), fileInfo.getSize()))
                .header("Content-Disposition", "inline; filename=\"" + fileInfo.getOriginalName() + "\"")
                .header("Cache-Control", "max-age=86400");
    }

    // READ - Download file
    @Get("/download/{id}")
    public HttpResponse<?> download(Long id) {
        Optional<FileInfo> optFile = fileService.findById(id);
        if (optFile.isEmpty()) {
            return HttpResponse.notFound();
        }
        FileInfo fileInfo = optFile.get();
        InputStream dataStream = fileService.downloadFileStream(fileInfo.getFilePath());
        return HttpResponse.ok(new StreamedFile(dataStream, MediaType.of(fileInfo.getContentType()), fileInfo.getSize()))
                .header("Content-Disposition", "attachment; filename=\"" + fileInfo.getOriginalName() + "\"");
    }

    // READ - Get file info
    @Get("/info/{id}")
    public HttpResponse<FileInfo> info(Long id) {
        return fileService.findById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    // READ - List files with pagination (for gallery)
    @Get("/list{?page,size}")
    public HttpResponse<Map<String, Object>> list(
            @Nullable @QueryValue(defaultValue = "1") Integer page,
            @Nullable @QueryValue(defaultValue = "20") Integer size) {
        List<FileInfo> allFiles = fileService.findByUserId(1L);
        int total = allFiles.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<FileInfo> pagedFiles = fromIndex < total
                ? allFiles.subList(fromIndex, toIndex)
                : List.of();

        return HttpResponse.ok(Map.of(
                "content", pagedFiles,
                "totalElements", total,
                "totalPages", (int) Math.ceil((double) total / size),
                "page", page,
                "size", size
        ));
    }

    // UPDATE - Replace file
    @Put(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA)
    public HttpResponse<FileInfo> update(@PathVariable Long id, @Part("file") CompletedFileUpload fileUpload) {
        try {
            Optional<FileInfo> optFile = fileService.findById(id);
            if (optFile.isEmpty()) {
                return HttpResponse.notFound();
            }

            FileInfo updated = fileService.updateFile(
                id,
                fileUpload.getFilename(),
                fileUpload.getInputStream(),
                fileUpload.getSize(),
                fileUpload.getContentType().orElse(MediaType.of("application/octet-stream")).toString()
            );
            return HttpResponse.ok(updated);
        } catch (IOException e) {
            return HttpResponse.serverError(new FileInfo());
        }
    }

    // DELETE - Delete file
    @Delete("/{id}")
    public HttpResponse<?> delete(@PathVariable Long id) {
        try {
            fileService.deleteFile(id);
            return HttpResponse.noContent();
        } catch (RuntimeException e) {
            return HttpResponse.notFound();
        }
    }
}

