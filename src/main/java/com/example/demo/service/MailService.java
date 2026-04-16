package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    // token → [email, password]
    private Map<String, String[]> tokenStore = new HashMap<>();

    public void saveToken(String email, String password, String token) {
        tokenStore.put(token, new String[] { email, password });
    }

    public String[] findByToken(String token) {
        return tokenStore.get(token);
    }

    public void sendMail(String to, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("認証メール");
        message.setText("以下のURLをクリックしてください\n" + url);

        mailSender.send(message);
    }
}