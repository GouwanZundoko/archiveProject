package com.example.demo.controller;

import com.example.demo.repository.PublicListRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import com.example.demo.sql.SelectPublicLists;
import com.example.demo.dto.MyListsDto;
import java.util.List;

@Controller
public class PublicListsController {

    private final PublicListRepository repository;
    @Autowired
    SelectPublicLists selectPublicLists;

    // ★ これを追加（重要）
    public PublicListsController(PublicListRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/public-lists")
    public String showLists(
            @RequestParam(required = false) String keyword, HttpSession session, Model model) {

        String CompanyId = (String) session.getAttribute("CompanyId");

        List<MyListsDto> lists = selectPublicLists.GetAllPublicLists(CompanyId, "1");

        model.addAttribute("lists", lists);

        return "public-lists";
    }
}