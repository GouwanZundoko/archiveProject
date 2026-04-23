package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import com.example.demo.sql.SelectUserInfo;

@Controller
public class LoginController {

    @Autowired
    SelectUserInfo selectUserInfo;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("companyCode") String _companyCode,
            @RequestParam("email") String _email,
            @RequestParam("password") String _password,
            HttpSession session) {
        // ログイン内容を取得
        String companyCode = _companyCode;
        String email = _email;
        String password = _password;
        // ログイン内容をSQLに変換
        List<Object[]> getResultList = selectUserInfo.GetUserInfoAll(companyCode, email, password);
        if (getResultList.size() > 0) {
            // ログイン成功時は公開コンテンツへリダイレクト
            String CompanyId = (String) getResultList.get(0)[0];
            String UserId = String.valueOf(getResultList.get(0)[1]);
            session.setAttribute("CompanyId", CompanyId);
            session.setAttribute("UserId", UserId);
            return "redirect:/public-lists";
        } else {
            return "login";
        }

    }

    @GetMapping("/login/password-reset")
    public String passwordReset() {
        return "password-reset";
    }
}