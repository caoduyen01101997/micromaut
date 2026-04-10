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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller("/files")
@Secured(SecurityRule.IS_ANONYMOUS)
@ExecuteOn(TaskExecutors.IO)
public class FileController {

    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

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
        try {
            InputStream dataStream = fileService.downloadFileStream(fileInfo.getFilePath());
            return HttpResponse.ok(new StreamedFile(dataStream, MediaType.of(fileInfo.getContentType()), fileInfo.getSize()))
                    .header("Content-Disposition", "inline; filename=\"" + fileInfo.getOriginalName() + "\"")
                    .header("Cache-Control", "max-age=86400");
        } catch (Exception e) {
            LOG.error("Error viewing file id={}: {}", id, e.getMessage(), e);
            return HttpResponse.serverError("Failed to view file");
        }
    }

    // READ - Download file
    @Get("/download/{id}")
    public HttpResponse<?> download(Long id) {
        Optional<FileInfo> optFile = fileService.findById(id);
        if (optFile.isEmpty()) {
            return HttpResponse.notFound();
        }
        FileInfo fileInfo = optFile.get();
        try {
            InputStream dataStream = fileService.downloadFileStream(fileInfo.getFilePath());
            return HttpResponse.ok(new StreamedFile(dataStream, MediaType.of(fileInfo.getContentType()), fileInfo.getSize()))
                    .header("Content-Disposition", "attachment; filename=\"" + fileInfo.getOriginalName() + "\"");
        } catch (Exception e) {
            LOG.error("Error downloading file id={}: {}", id, e.getMessage(), e);
            return HttpResponse.serverError("Failed to download file");
        }
    }

    // READ - Get file info
    @Get("/info/{id}")
    public HttpResponse<FileInfo> info(Long id) {
        return fileService.findById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    // READ - List files with Pageable (optimized performance)
    @Get("/list")
    public HttpResponse<Map<String, Object>> list(
            io.micronaut.data.model.Pageable pageable,
            @Nullable @QueryValue String search) {
        io.micronaut.data.model.Page<FileInfo> pageResult = fileService.listFiles(search, pageable);
        
        return HttpResponse.ok(Map.of(
                "content", pageResult.getContent(),
                "totalElements", pageResult.getNumberOfElements(),
                "totalPages", pageResult.getTotalPages(),
                "page", pageResult.getPageNumber() + 1,  // 1-based for frontend
                "size", pageResult.getSize()
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

