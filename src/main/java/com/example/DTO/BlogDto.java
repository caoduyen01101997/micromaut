package com.example.DTO;

import com.example.document.Blog;
import com.example.document.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlogDto {
    private Long id;
    private String author;
    private String title;
    private String content;
    private int star;
    private int timeToRead;
    private Long userId;

    // make a new blog from blogDto
    public static  Blog toEntity(BlogDto blogDto, User user) {
        return new Blog(
                blogDto.getId(),
                blogDto.getAuthor(),
                new Date(),
                new Date(),
                blogDto.getTitle(),
                blogDto.getContent(),
                blogDto.getStar(),
                blogDto.getTimeToRead(),
                user
        );
    }
}
