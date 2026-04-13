package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean publicFlag;
    private LocalDate createdAt;

    @ManyToMany
    private List<Content> contents;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setPublicFlag(boolean publicFlag) {
        this.publicFlag = publicFlag;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublicFlag() {
        return publicFlag;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}