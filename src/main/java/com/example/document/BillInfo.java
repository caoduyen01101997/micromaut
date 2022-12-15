package com.example.document;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
@MappedEntity
public class BillInfo {
    @Id
    @GeneratedValue
    private String id;
    @NonNull
    @NotBlank
    private String order;
    @NonNull
    @NotBlank
    private String name;


}
