package com.example.DTO;

import com.example.document.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String role;

    public UserDto toDto(User user) {
        return UserDto.builder().username(user.getUsername()).password(user.getPassword()).role(user.getRole()).build();
    }


}

