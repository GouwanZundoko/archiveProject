package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordResetController {

    @GetMapping("/password-reset")
    public String showPasswordResetForm() {
        return "password-reset";
    }

    @PostMapping("/password-reset")
    public String handlePasswordReset(
            @RequestParam("email") String email,
            Model model) {

        // TODO: 本来はここでメール送信処理
        System.out.println("Password reset mail sent to: " + email);

        model.addAttribute("message", "パスワード再発行メールを送信しました");

        return "password-reset";
    }
}