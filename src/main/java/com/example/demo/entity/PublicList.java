package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class PublicList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String author;

    private boolean publicFlag;

    private LocalDate createdAt;

    @ManyToMany
    @JoinTable(name = "public_list_contents", joinColumns = @JoinColumn(name = "list_id"), inverseJoinColumns = @JoinColumn(name = "content_id"))
    private List<Content> contents;
}