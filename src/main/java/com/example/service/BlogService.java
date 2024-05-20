package com.example.service;

import com.example.document.Blog;
import com.example.document.User;
import com.example.repository.BlogRepository;
import com.example.repository.UserRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Date;
import java.util.Optional;
@Singleton
public class BlogService {
    @Inject
    private BlogRepository blogRepository;
    public Iterable<Blog> list() {
        return blogRepository.findAll();
    }

    public Blog save(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreatedDate(new Date());
            blog.setUpdatedDate(new Date());
            return blogRepository.save(blog);
        } else {
            blog.setUpdatedDate(new Date());
            return blogRepository.update(blog);
        }
    }

    public Optional<Blog> find(@NonNull Long id) {
        return blogRepository.findById(id);
    }
}
