package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.example.demo.sql.SelectMyLists;
import com.example.demo.sql.SelectMyContents;
import com.example.demo.dto.MyContentsDto;
import com.example.demo.dto.MyContentsListDto;
import com.example.demo.dto.MyListsDto;

@Controller
@RequestMapping("/my-lists")
public class MyListController {

    @Autowired
    SelectMyLists selectMyLists;

    @Autowired
    private SelectMyContents selectMyContents;

    @GetMapping
    public String list(HttpSession session, Model model) {

        String userId = (String) session.getAttribute("UserId");
        String companyId = (String) session.getAttribute("CompanyId");

        // マイリスト
        List<MyListsDto> lists = selectMyLists.GetAllMyLists(userId, companyId);
        model.addAttribute("lists", lists);

        // ★モーダル用コンテンツ
        List<MyContentsDto> contentsList = selectMyContents.GetAllMyContents(userId, companyId);
        model.addAttribute("contentsList", contentsList);

        return "my-lists";
    }

    @PostMapping("/create-list")
    public String handlePasswordReset(HttpSession session, String listsTitle, String listsText, Model model) {
        String userId = (String) session.getAttribute("UserId");
        String companyId = (String) session.getAttribute("CompanyId");
        Long count = selectMyLists.InsertAllPublicLists(userId, companyId, listsTitle, listsText);
        selectMyLists.InsertMyLists(userId, companyId, count);
        return "redirect:/my-lists";
    }

    @GetMapping("/selected-contents/{listId}")
    @ResponseBody
    public List<MyContentsListDto> getSelectedContents(@PathVariable String listId) {
        return selectMyLists.getContentsByListId(listId);
    }

    @PostMapping("/add-contents")
    @ResponseBody
    public String addContents(@RequestBody Map<String, Object> body, HttpSession session) {

        String userId = (String) session.getAttribute("UserId");
        String companyId = (String) session.getAttribute("CompanyId");
        String listId = (String) body.get("listId");
        List<?> rawList = (List<?>) body.get("contentsIds");

        List<String> contentsIds = new ArrayList<>();
        for (Object obj : rawList) {
            contentsIds.add(String.valueOf(obj));
        }

        // 既存削除処理
        selectMyLists.DeleteMyContentsList(userId, companyId, listId);

        // 追加処理
        selectMyLists.InsertPublicList(listId, userId, companyId, contentsIds);

        return "redirect:/my-lists";
    }
}