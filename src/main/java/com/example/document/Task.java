package com.example.document;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import lombok.Data;

@Data
@Entity(name = "task")
public class Task {
    @javax.persistence.Id
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reminder_date")
    private Date reminderDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "resolved_date")
    private Date resolveDate;

    private int status;
}
