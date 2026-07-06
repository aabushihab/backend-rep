package com.clinic.doctor_app_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "folders")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "item_ids", columnDefinition = "jsonb")
    private List<Long> itemIds = new ArrayList<>();
}