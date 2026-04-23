package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.example.demo.sql.SelectMyContents;
import com.example.demo.sql.SelectMyOneContents;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.demo.dto.MyContentsDto;

@Controller
public class MyContentsController {

    @Autowired
    SelectMyContents selectMyContents;

    @Autowired
    SelectMyOneContents selectMyOneContents;

    @GetMapping("/my-contents")
    public String showContentsPage(HttpSession session, Model model) {

        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");

        List<MyContentsDto> lists = selectMyContents.GetAllMyContents(UserId, CompanyId);
        for (MyContentsDto dto : lists) {
            if (dto.getTag() != null && !dto.getTag().isEmpty()) {
                List<String> tags = Arrays.asList(dto.getTag().split(","));
                dto.setContentsTagArray(tags);
            } else {
                dto.setContentsTagArray(new ArrayList<>());
            }
        }

        model.addAttribute("lists", lists);

        return "my-contents";
    }

    @GetMapping("/contents/release/{id}/{release}")
    public String contentsRelease(HttpSession session, Model model, @PathVariable("id") String productId,
            @PathVariable("release") String releaseFlg) {
        // DB取得は自分で実装
        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");
        int selectCount = selectMyContents.UpdateMyContents(UserId, CompanyId, productId, releaseFlg);
        return "redirect:/my-contents";
    }

    @GetMapping("/contents/delete/{id}")
    public String contentsDelete(HttpSession session, Model model, @PathVariable("id") String productId) {
        // DB取得は自分で実装
        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");
        // DTO使用
        List<MyContentsDto> lists = selectMyOneContents.GetMyContents(UserId, CompanyId, productId);
        MyContentsDto dto = lists.get(0);
        String Auth = dto.getShowAuth();
        // マイコンテンツ側を削除
        int deleteMyCount = selectMyContents.DeleteMyContents(UserId, CompanyId, productId);

        if ("1".equals(Auth)) {
            // 自分で作成したコンテンツの場合コンテンツと画像も削除
            // ===== 保存先ベース =====
            String baseDir = "C:/app/uploads/";

            // サムネ削除
            deleteFile(baseDir, dto.getImageUrl());

            // 動画削除
            deleteFile(baseDir, dto.getMovieUrl());

            // ファイル削除
            deleteFile(baseDir, dto.getFileUrl());

            int deletePublicCount = selectMyContents.DeletePublicContents(UserId, CompanyId, productId);
        }

        return "redirect:/my-contents";
    }

    private void deleteFile(String baseDir, String url) {

        // URLチェック（ここが一番重要）
        if (url == null || url.isEmpty()) {
            return;
        }

        // /files/ を除去（必要なら）
        String relativePath = url.replace("/files/", "");

        Path path = Paths.get(baseDir, relativePath);

        try {
            // デバッグ（原因調査用）
            System.out.println("削除対象: " + path);
            System.out.println("存在: " + Files.exists(path));
            System.out.println("ファイル: " + Files.isRegularFile(path));

            // ファイルだけ削除
            if (Files.isRegularFile(path)) {
                Files.delete(path);
                System.out.println("削除成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}