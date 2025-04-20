package com.example.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatPageController {

    @GetMapping("/chat")
    public String chatPage(@RequestParam String roomId,
                           @RequestParam String userName,
                           Model model) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("userName", userName);
        return "chat";
    }
}
