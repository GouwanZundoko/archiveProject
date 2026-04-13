package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import com.example.demo.sql.InsertPublicContents;
import com.example.demo.sql.InsertMyContents;
import com.example.demo.sql.SelectMyOneContents;
import com.example.demo.dto.MyContentsDto;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/contents")
public class ContentController {

    @Autowired
    InsertPublicContents insertPublicContents;
    @Autowired
    InsertMyContents insertMyContents;
    @Autowired
    SelectMyOneContents selectMyOneContents;

    @GetMapping("/create")
    public String create(Model model) {
        // 仮データ（画面表示用）
        model.addAttribute("contentsId", "none");
        model.addAttribute("mode", "create");
        model.addAttribute("content", new MyContentsDto());
        return "create-content";
    }

    @GetMapping("/edit/{id}")
    public String edit(HttpSession session, Model model, @PathVariable("id") String productId) {
        // DB取得は自分で実装
        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");
        List<MyContentsDto> contents = selectMyOneContents.GetMyContents(UserId, CompanyId, productId);
        MyContentsDto dto = contents.get(0);
        model.addAttribute("contentsId", productId);
        model.addAttribute("mode", "edit");
        model.addAttribute("content", dto);
        return "create-content";
    }

    @PostMapping("/save")
    public String saveContent(
            HttpSession session,
            @RequestParam String contentsId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) MultipartFile thumbnailFile,
            @RequestParam(required = false) MultipartFile videoFile,
            @RequestParam(required = false) MultipartFile documentFile,
            @RequestParam String imageUrl,
            @RequestParam String movieUrl,
            @RequestParam String fileUrl) {

        String userId = (String) session.getAttribute("UserId");
        String companyId = (String) session.getAttribute("CompanyId");

        // ===== 保存先ベース =====
        String baseDir = "C:/app/uploads/";

        // ===== ファイル保存してパス取得 =====
        String thumbnailPath = "";
        if (thumbnailFile == null || thumbnailFile.isEmpty()) {
            thumbnailPath = imageUrl;
        } else {
            thumbnailPath = saveFile(thumbnailFile, baseDir + "thumbnail/");
        }

        String videoPath = "";
        if (videoFile == null || videoFile.isEmpty()) {
            videoPath = movieUrl;
        } else {
            videoPath = saveFile(videoFile, baseDir + "video/");
        }

        String documentPath = "";
        if (documentFile == null || documentFile.isEmpty()) {
            documentPath = fileUrl;
        } else {
            documentPath = saveFile(documentFile, baseDir + "document/");
        }

        if (!"none".equals(contentsId)) {
            // ===== DB登録（パスを渡す）=====
            insertPublicContents.UpdateContents(
                    contentsId,
                    userId,
                    companyId,
                    title,
                    description,
                    tags,
                    thumbnailPath,
                    videoPath,
                    documentPath);
        } else {
            // ===== DB登録（パスを渡す）=====
            Long contentsLong = insertPublicContents.InsertContents(
                    userId,
                    companyId,
                    title,
                    description,
                    tags,
                    thumbnailPath,
                    videoPath,
                    documentPath);

            insertMyContents.InsertContents(userId, companyId, contentsLong);
        }

        return "redirect:/my-contents";
    }

    private String saveFile(MultipartFile file, String dir) {
        if (file == null || file.isEmpty()) {
            return "";
        }

        try {
            // ディレクトリ作成
            Path dirPath = Paths.get(dir);
            Files.createDirectories(dirPath);

            // ファイル名（重複防止）
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // 保存
            Path filePath = dirPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // ★ DBに保存するのはURLパス
            String folder = dirPath.getFileName().toString();
            return "/files/" + folder + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("ファイル保存失敗", e);
        }
    }
}