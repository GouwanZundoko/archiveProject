package com.example.demo.repository;

import com.example.demo.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByPublicFlagTrue();

    List<Content> findByPublicFlagTrueAndTitleContainingIgnoreCase(String keyword);

    List<Content> findByPublicFlagTrueAndType(String type);
}