package com.example.webchat.web.chat;

import com.example.webchat.chat.room.service.ChatRoomService;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    private final ChatRoomService chatRoomService;

    public HomeController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping({"", "/index"})
    public String home(HttpServletResponse response) {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("rooms", chatRoomService.findRoomInfos());
        return "chat/home";
    }

}
