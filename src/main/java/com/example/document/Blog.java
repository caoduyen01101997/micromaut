package com.example.document;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
@Entity(name = "blog")
public class Blog {
    @javax.persistence.Id
    @Id
    @GeneratedValue
    private Long id;

    private String author;

    private Date createdDate;

    private Date updatedDate;

    private String description;

    private String content;

    private int star;

    private int timeToRead;
}
