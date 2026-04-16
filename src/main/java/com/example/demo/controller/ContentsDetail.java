package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.dto.MyContentsDto;
import com.example.demo.dto.MyContentsListDto;
import com.example.demo.sql.SelectMyLists;
import com.example.demo.sql.SelectMyOneContents;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import java.nio.file.Path;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import org.springframework.http.MediaType;

@Controller
public class ContentsDetail {

    @Autowired
    SelectMyOneContents selectMyOneContents;

    @Autowired
    SelectMyLists selectMyLists;

    @GetMapping("/contents/view/{id}")
    public String contentDetail(HttpSession session, Model model, @PathVariable("id") String productId,
            @RequestParam(value = "listId", required = false) String listId) {
        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");

        // DTO使用
        List<MyContentsDto> lists = selectMyOneContents.GetMyContents(UserId, CompanyId, productId);

        // 仮データ（DTO使わない）
        // 例：先頭1件を表示
        MyContentsDto dto = lists.get(0);

        // 画面用に変換
        Map<String, Object> content = new HashMap<>();

        content.put("id", dto.getContentsId());
        content.put("title", dto.getContentsTitle());
        content.put("description", dto.getContentsText());
        content.put("thumbnailUrl", dto.getImageUrl());
        content.put("movieUrl", dto.getMovieUrl());
        content.put("fileUrl", dto.getFileUrl());

        // type判定（重要）
        String type;
        if (dto.getMovieUrl() != null && !dto.getMovieUrl().isEmpty()) {
            type = "video";
        } else if (dto.getFileUrl() != null && !dto.getFileUrl().isEmpty()) {
            type = "document";
        } else {
            type = "markup";
        }
        content.put("type", type);

        // URL（typeに応じて切り替え）
        String url = null;
        if ("video".equals(type)) {
            url = dto.getMovieUrl();
        } else if ("document".equals(type)) {
            url = dto.getFileUrl();
        }
        content.put("url", url);

        content.put("author", dto.getUserId());
        content.put("createdAt", dto.getCreateDate());

        // タグ
        List<String> tags = Arrays.asList(dto.getTag().split(","));

        model.addAttribute("content", content);
        model.addAttribute("tags", tags);

        // リスト内コンテンツ取得追加
        List<MyContentsListDto> listContents = new ArrayList<>();

        if (listId != null) {
            listContents = selectMyLists.getContentsByListId(listId);
        }

        model.addAttribute("listContents", listContents);
        model.addAttribute("currentId", productId);
        model.addAttribute("listId", listId);

        return "mycontent-detail";
    }

    @GetMapping("/download/files/document/{fileName}")
    public ResponseEntity<Resource> download(HttpSession session, @PathVariable String fileName) throws IOException {

        Path path = Paths.get("C:/app/uploads/document/" + fileName);
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // ←これ重要
                .body(resource);
    }
}