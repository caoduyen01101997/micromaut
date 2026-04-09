package com.example.repository;

import com.example.document.FileInfo;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.jpa.repository.JpaSpecificationExecutor;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long>, JpaSpecificationExecutor<FileInfo> {
    Optional<FileInfo> findByFilePath(String filePath);
    
    Page<FileInfo> findAll(Pageable pageable);
}
