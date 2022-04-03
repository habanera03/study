package com.example.webchat.web.chat;

import com.example.webchat.chat.room.service.ChatRoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ChatRoomService chatRoomService;

    public HomeController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping({"", "/index"})
    public String home() {
        return "redirect:/chat/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("rooms", chatRoomService.findRoomInfos());
        return "chat/home";
    }

}
