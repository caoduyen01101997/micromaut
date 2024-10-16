package com.example.Mapper;

import com.example.DTO.UserDto;
import com.example.document.User;
import jakarta.inject.Singleton;

@Singleton
public class Mapper {
    // public User toEntity(UserDto userDto) {
    //     return User.builder().username(userDto.getUsername()).password(userDto.getPassword()).role(userDto.getRole()).build();
    // }

    public UserDto toDto(User user) {
        return UserDto.builder().username(user.getUsername()).password(user.getPassword()).role(user.getRole()).build();
    }
}
