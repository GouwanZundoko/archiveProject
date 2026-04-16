package com.example.demo.controller;

import com.example.demo.service.MailService;
import com.example.demo.sql.InsertRegistSQL;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class RegistController {

    @Autowired
    InsertRegistSQL registSQL;

    @Autowired
    private MailService mailService;

    @Autowired
    private HttpSession session;

    @GetMapping("/regist")
    public String showRegistPage() {
        return "regist";
    }

    @PostMapping("/send-mail")
    @ResponseBody
    public String sendMail(@RequestParam String email,
            @RequestParam String password) {

        String token = UUID.randomUUID().toString();

        // email + password を保存
        mailService.saveToken(email, password, token);

        String url = "http://localhost:8080/verify?token=" + token;

        mailService.sendMail(email, url);

        return "メール送信完了";
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String token) {

        String[] data = mailService.findByToken(token);

        if (data == null) {
            return "error";
        }

        String email = data[0];
        String password = data[1];

        session.setAttribute("email", email);
        session.setAttribute("password", password);

        return "redirect:/complete";
    }

    @GetMapping("/complete")
    public String complete(Model model) {
        String password = (String) session.getAttribute("password");
        String email = (String) session.getAttribute("email");
        String result = "";
        int registCount = registSQL.GetUserInfo(email);
        if (email == null || password == null) {
            result = "データが存在しません";
        } else if (registCount > 0) {
            result = "既に登録されています";
        } else {
            registSQL.InsertUserInfo(email, password);
            result = "登録完了しました";
        }
        // セッションを破棄して見せない
        session.invalidate();
        model.addAttribute("result", result);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        return "complete";
    }

}