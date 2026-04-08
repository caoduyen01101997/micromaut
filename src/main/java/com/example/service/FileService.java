package com.example.service;

import com.example.document.FileInfo;
import com.example.repository.FileInfoRepository;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    @Value("${micronaut.object-storage.aws.default.bucket:my-bucket}")
    private String bucketName;

    @Inject
    private FileInfoRepository fileInfoRepository;

    @Inject
    private S3Client s3Client;

    // CREATE - Upload file
    public FileInfo uploadFile(String originalName, String name, InputStream inputStream, long size, String contentType, Long userId) {
        String key = UUID.randomUUID().toString() + "_" + originalName;

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, size));

        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalName(originalName);
        fileInfo.setName(name);
        fileInfo.setFilePath(key);
        fileInfo.setContentType(contentType);
        fileInfo.setSize(size);
        fileInfo.setUploadDate(new Date());
        fileInfo.setUserId(userId);

        return fileInfoRepository.save(fileInfo);
    }

    // READ - Download file stream for large files
    public InputStream downloadFileStream(String filePath) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();

        try {
            return s3Client.getObject(getRequest);
        } catch (Exception e) {
            LOG.error("Error downloading file: {}", filePath, e);
            throw new RuntimeException("Download failed", e);
        }
    }

    // READ - Find FileInfo by id
    public Optional<FileInfo> findById(Long id) {
        return fileInfoRepository.findById(id);
    }

    // READ - Find FileInfo by filePath
    public Optional<FileInfo> findByFilePath(String filePath) {
        return fileInfoRepository.findByFilePath(filePath);
    }

    // READ - List files by userId
    public List<FileInfo> findByUserId(Long userId) {
        return fileInfoRepository.findAll();
    }

    // UPDATE - Replace file content
    public FileInfo updateFile(Long id, String originalName, InputStream inputStream, long size, String contentType) {
        Optional<FileInfo> optFile = fileInfoRepository.findById(id);
        if (optFile.isEmpty()) {
            throw new RuntimeException("File not found with id: " + id);
        }

        FileInfo fileInfo = optFile.get();

        // Delete old file from S3
        deleteFromS3(fileInfo.getFilePath());

        // Upload new file with new key
        String newKey = UUID.randomUUID().toString() + "_" + originalName;

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(newKey)
                .contentType(contentType)
                .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, size));

        fileInfo.setOriginalName(originalName);
        fileInfo.setFilePath(newKey);
        fileInfo.setContentType(contentType);
        fileInfo.setSize(size);

        return fileInfoRepository.update(fileInfo);
    }

    // DELETE - Delete file
    public void deleteFile(Long id) {
        Optional<FileInfo> optFile = fileInfoRepository.findById(id);
        if (optFile.isEmpty()) {
            throw new RuntimeException("File not found with id: " + id);
        }

        FileInfo fileInfo = optFile.get();
        deleteFromS3(fileInfo.getFilePath());
        fileInfoRepository.deleteById(id);
    }

    // Delete from S3 storage
    private void deleteFromS3(String filePath) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();

        s3Client.deleteObject(deleteRequest);
        LOG.info("Deleted file from S3: {}", filePath);
    }
}
