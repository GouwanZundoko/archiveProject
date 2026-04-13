package com.example.demo.repository;

import com.example.demo.entity.PublicList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicListRepository extends JpaRepository<PublicList, Long> {

    List<PublicList> findByPublicFlagTrue();

    List<PublicList> findByPublicFlagTrueAndTitleContainingIgnoreCase(String keyword);
}