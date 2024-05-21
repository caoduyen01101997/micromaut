package com.example.repository;

import com.example.document.Blog;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
}
