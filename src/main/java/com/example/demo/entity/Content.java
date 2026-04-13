package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Content {

    @Id // ← 必須
    private String id;

    private String title;
    private String description;
    private String type;
    private String thumbnailUrl;

    @ElementCollection // ← List<String> を保存するために必要
    private List<String> tags;
    private boolean publicFlag;

    // ★ JPA必須：デフォルトコンストラクタ
    protected Content() {
    }

    // 通常用コンストラクタ
    public Content(String id, String title, String description,
            String type, String thumbnailUrl,
            List<String> tags, boolean isPublic) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.thumbnailUrl = thumbnailUrl;
        this.tags = tags;
        this.publicFlag = isPublic;
    }

    // getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean isPublicFlag() {
        return publicFlag;
    }
}