package com.example.demo.controller;

import com.example.demo.repository.PublicListRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;

import com.example.demo.sql.SelectMyContents;
import com.example.demo.sql.SelectMyLists;
import com.example.demo.sql.SelectPublicLists;
import com.example.demo.dto.MyContentsDto;
import com.example.demo.dto.MyContentsListDto;
import com.example.demo.dto.MyListsDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class PublicListsController {

    private final PublicListRepository repository;
    @Autowired
    SelectPublicLists selectPublicLists;

    @Autowired
    SelectMyLists selectMyLists;

    @Autowired
    private SelectMyContents selectMyContents;

    // ★ これを追加（重要）
    public PublicListsController(PublicListRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/public-lists")
    public String showLists(
            @RequestParam(required = false) String keyword, HttpSession session, Model model) {

        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");

        List<MyListsDto> lists = selectPublicLists.GetAllPublicLists(UserId, CompanyId, "1");
        model.addAttribute("lists", lists);

        // ★モーダル用コンテンツ
        List<MyContentsDto> contentsList = selectMyContents.GetAllMyContents(UserId, CompanyId);
        model.addAttribute("contentsList", contentsList);

        return "public-lists";
    }

    @GetMapping("/public-lists/selected-contents/{listId}")
    @ResponseBody
    public List<MyContentsListDto> getSelectedContents(@PathVariable String listId) {
        return selectMyLists.getContentsByListId(listId);
    }

    @PostMapping("/public-lists/add-contents")
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

        return "redirect:/public-lists";
    }

    @GetMapping("/public-lists/release/{id}/{release}")
    public String contentsRelease(HttpSession session, Model model, @PathVariable("id") String productId,
            @PathVariable("release") String releaseFlg) {
        // DB取得は自分で実装
        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");
        int selectCount = selectMyLists.UpdateMyLists(UserId, CompanyId, productId, releaseFlg);
        return "redirect:/public-lists";
    }

    @GetMapping("/public-lists/delete/{id}")
    public String listsDelete(HttpSession session, Model model, @PathVariable("id") String productId) {
        // DB取得は自分で実装
        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");
        // DTO使用
        List<MyListsDto> lists = selectMyLists.GetOneMyLists(UserId, CompanyId, productId);
        MyListsDto dto = lists.get(0);
        String Auth = dto.getShowAuth();
        // マイコンテンツ側を削除
        int deleteMyCount = selectMyLists.DeleteMyLists(UserId, CompanyId, productId);

        if ("1".equals(Auth)) {
            // 自分で作成したコンテンツの場合コンテンツも削除
            int deletePublicCount = selectMyLists.DeletePublicLists(UserId, CompanyId, productId);
            deletePublicCount = selectMyLists.DeletePublicContentsList(UserId, CompanyId, productId);
        }

        return "redirect:/public-lists";
    }

    @GetMapping("/public-lists/import/{id}")
    public String contentsImport(HttpSession session, Model model, @PathVariable("id") String productId) {
        // DB取得は自分で実装
        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");
        selectPublicLists.ImportPublicLists(UserId, CompanyId, productId);
        return "redirect:/public-lists";
    }
}