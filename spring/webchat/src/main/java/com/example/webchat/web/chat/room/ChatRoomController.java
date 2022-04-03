package com.example.webchat.web.chat.room;

import com.example.webchat.chat.room.service.ChatRoomService;
import com.example.webchat.web.security.JwtTokenProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chat")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final JwtTokenProvider jwtTokenProvider;

    public ChatRoomController(ChatRoomService chatRoomService, JwtTokenProvider jwtTokenProvider) {
        this.chatRoomService = chatRoomService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/room")
    public String create(@ModelAttribute ChatRoomRequest chatRoomRequest) {
        log.info("roomName : {}", chatRoomRequest.getRoomName());
        chatRoomService.create(chatRoomRequest.getRoomName());
        return "redirect:/home";
    }

    @GetMapping("/room/{id}")
    public String enter(@PathVariable String id, Model model) {
        model.addAttribute("roomId", id);
        return "chat/room";
    }

    @GetMapping("/connection")
    @ResponseBody
    public LoginInfo connection() {
        Authentication auth = getAuthentication();
        return new LoginInfo(auth.getName(), jwtTokenProvider.generateToken(auth.getName()));
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Getter
    private static class ChatRoomRequest {

        private final String roomName;

        public ChatRoomRequest(String roomName) {
            this.roomName = roomName;
        }
    }

    @Getter
    private static class LoginInfo {

        private final String name;
        private final String token;

        public LoginInfo(String name, String token) {
            this.name = name;
            this.token = token;
        }
    }
}
