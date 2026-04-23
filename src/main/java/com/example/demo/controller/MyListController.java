package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
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

        for (MyListsDto dto : lists) {
            if (dto.getListstag() != null && !dto.getListstag().isEmpty()) {
                List<String> tags = Arrays.asList(dto.getListstag().split(","));
                dto.setListsTagArray(tags);
            } else {
                dto.setListsTagArray(new ArrayList<>());
            }
        }

        model.addAttribute("lists", lists);

        // ★モーダル用コンテンツ
        List<MyContentsDto> contentsList = selectMyContents.GetAllMyContents(userId, companyId);

        for (MyContentsDto dto : contentsList) {
            if (dto.getTag() != null && !dto.getTag().isEmpty()) {
                List<String> tags = Arrays.asList(dto.getTag().split(","));
                dto.setContentsTagArray(tags);
            } else {
                dto.setContentsTagArray(new ArrayList<>());
            }
        }

        model.addAttribute("contentsList", contentsList);

        return "my-lists";
    }

    @PostMapping("/create-list")
    public String handlePasswordReset(HttpSession session, String listsTitle, String listsText, String listsTag,
            Model model) {
        String userId = (String) session.getAttribute("UserId");
        String companyId = (String) session.getAttribute("CompanyId");
        Long count = selectMyLists.InsertAllPublicLists(userId, companyId, listsTitle, listsText, listsTag);
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

    @GetMapping("/release/{id}/{release}")
    public String contentsRelease(HttpSession session, Model model, @PathVariable("id") String productId,
            @PathVariable("release") String releaseFlg) {
        // DB取得は自分で実装
        String UserId = (String) session.getAttribute("UserId");
        String CompanyId = (String) session.getAttribute("CompanyId");
        int selectCount = selectMyLists.UpdateMyLists(UserId, CompanyId, productId, releaseFlg);
        return "redirect:/my-lists";
    }

    @GetMapping("/delete/{id}")
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

        return "redirect:/my-lists";
    }
}