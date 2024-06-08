package com.example.document;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
@Entity(name = "blog")
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @Id
    @GeneratedValue
    private Long id;

    private String author;

    @Column(name = "createddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updatedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    private String description;

    private String content;

    private int star;

    @Column(name = "timetoread")
    private int timeToRead;

    @PrePersist
    protected void onCreate() {
        updatedDate = createdDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }
}