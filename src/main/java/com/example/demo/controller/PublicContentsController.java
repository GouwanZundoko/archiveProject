package com.example.demo.controller;

import com.example.demo.repository.ContentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import com.example.demo.sql.SelectPublicContents;
import com.example.demo.dto.MyContentsDto;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Controller
public class PublicContentsController {

    private final ContentRepository contentRepository;
    @Autowired
    SelectPublicContents selectPublicContents;

    // ★ 手書きコンストラクタ追加
    public PublicContentsController(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @GetMapping("/public-contents")
    public String showPublicContents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            HttpSession session,
            Model model) {

        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");

        List<MyContentsDto> lists = selectPublicContents.GetAllPublicContents(UserId, CompanyId, "1");

        model.addAttribute("lists", lists);

        return "public-contents";
    }

    @GetMapping("/contents/import/{id}")
    public String contentsImport(HttpSession session, Model model, @PathVariable("id") String productId) {
        // DB取得は自分で実装
        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");
        selectPublicContents.ImportPublicContents(UserId, CompanyId, productId);
        return "redirect:/public-contents";
    }
}