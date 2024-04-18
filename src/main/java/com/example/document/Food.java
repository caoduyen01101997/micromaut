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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
@Entity
public class Food {
    @javax.persistence.Id
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @NotBlank
    private String name;

    @Id
    private String catagoryId;

    private double price;
}
