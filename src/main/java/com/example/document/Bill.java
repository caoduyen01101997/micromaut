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
@Entity
public class Bill {
    @javax.persistence.Id
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @NotBlank
    private String billCode;

    @NonNull
    private Date dateCheckIn;
    private Date dateCheckOut;
    private boolean isPayed;

    private Table tableId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
