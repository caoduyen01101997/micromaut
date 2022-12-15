package com.example.document;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
@MappedEntity
public class Table implements Serializable {
    @Id
    @GeneratedValue
    private String id;

    @NonNull
    @NotBlank
    private  String name;
    @Nullable
    private int order;
    @Nullable
    private String description;
    @Nullable
    private int orderStatus;
//    @Nullable
//    private String locationId;
}
