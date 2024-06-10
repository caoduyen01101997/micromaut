package com.example.repository;

import java.util.Optional;
import java.util.List; // Add this import statement

import com.example.document.Blog;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    // Optional<Blog> findById(Long id);
}
