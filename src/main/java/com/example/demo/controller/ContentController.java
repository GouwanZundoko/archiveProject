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

    // saveContent の引数を変更
    @PostMapping("/save")
    public String saveContent(
            HttpSession session,
            @RequestParam String contentsId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) String tags,

            // ★ thumbnailFile → Base64文字列で受け取る
            @RequestParam(required = false) String thumbnailFile,

            @RequestParam(required = false) MultipartFile videoFile,
            @RequestParam(required = false) MultipartFile documentFile,
            @RequestParam String imageUrl,
            @RequestParam String movieUrl,
            @RequestParam String fileUrl) {

        String userId = (String) session.getAttribute("UserId");
        String companyId = (String) session.getAttribute("CompanyId");

        String baseDir = "C:/app/uploads/";

        // ===== サムネ保存（新関数）=====
        String thumbnailPath = saveThumbnailFromImg(
                thumbnailFile,
                baseDir + "thumbnail/",
                imageUrl);

        // ===== 動画 =====
        String videoPath = "";
        if (videoFile == null || videoFile.isEmpty()) {
            videoPath = movieUrl;
        } else {
            videoPath = saveFile(videoFile, baseDir + "video/", movieUrl);
        }

        // ===== ファイル =====
        String documentPath = "";
        if (documentFile == null || documentFile.isEmpty()) {
            documentPath = fileUrl;
        } else {
            documentPath = saveFile(documentFile, baseDir + "document/", fileUrl);
        }

        if (!"none".equals(contentsId)) {
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

    private String saveFile(MultipartFile file, String dir, String oldFileUrl) {
        if (file == null || file.isEmpty()) {
            return "";
        }

        try {

            // ディレクトリ作成
            Path dirPath = Paths.get(dir);
            Files.createDirectories(dirPath);

            // 旧ファイル削除
            if (oldFileUrl != null && !(oldFileUrl.isEmpty())) {
                // /files/content/test.pdf
                String oldFileName = Paths.get(oldFileUrl).getFileName().toString();
                // 実ファイルパスに変換
                Path oldFilePath = dirPath.resolve(oldFileName);
                Files.deleteIfExists(oldFilePath);
            }

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

    // ★ 新規追加：imgタグ(thumbnailPreview)のbase64を保存する専用関数
    private String saveThumbnailFromImg(
            String base64Image,
            String dir,
            String oldFileUrl) {

        if (base64Image == null || base64Image.isBlank()) {
            return oldFileUrl != null ? oldFileUrl : "";
        }

        try {
            Path dirPath = Paths.get(dir);
            Files.createDirectories(dirPath);

            // 旧ファイル削除
            if (oldFileUrl != null && !oldFileUrl.isBlank()) {
                String oldFileName = Paths.get(oldFileUrl)
                        .getFileName()
                        .toString();

                Path oldFilePath = dirPath.resolve(oldFileName);
                Files.deleteIfExists(oldFilePath);
            }

            // data:image/png;base64,xxxxx
            String[] parts = base64Image.split(",");
            if (parts.length < 2) {
                return oldFileUrl != null ? oldFileUrl : "";
            }

            byte[] imageBytes = java.util.Base64.getDecoder().decode(parts[1]);

            String fileName = UUID.randomUUID() + "_thumbnail.png";

            Path filePath = dirPath.resolve(fileName);

            Files.write(filePath, imageBytes);

            return "/files/thumbnail/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("サムネ保存失敗", e);
        }
    }
}