package com.example.service;

import com.example.DTO.BlogDto;
import com.example.document.Blog;

import com.example.document.User;
import com.example.repository.BlogRepository;
import com.example.repository.UserRepository;
import com.example.util.IdUtil;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Singleton
public class BlogService {
    @Inject
    private BlogRepository blogRepository;
    @Inject
    private UserRepository userRepository;
    public Page<Blog> list(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Transactional
    public Blog save(BlogDto blogDto) {
        User user = userRepository.findById(blogDto.getUserId()).orElseThrow(() -> null);
        Blog blog = BlogDto.toEntity(blogDto, user);
        blog.setId(IdUtil.generateId());
        return blogRepository.save(blog);
    }

    public Blog update(Blog blog) {
        blog.setUpdatedDate(new Date());
        return blogRepository.update(blog);
    }

    public Optional<Blog> find(@NonNull Long id) {
        return blogRepository.findById(id);
    }

    public int  delete(Long id) {
        blogRepository.deleteById(id);

        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isEmpty()) {
            return 1;
        } else {
            return 0;
        }
    }
}
