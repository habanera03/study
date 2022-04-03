package com.example.webchat.web.chat.room;

import com.example.webchat.chat.room.service.ChatRoomService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/room")
    public String create(@ModelAttribute ChatRoomRequest chatRoomRequest) {
        log.info("roomName : {}", chatRoomRequest.getRoomName());
        chatRoomService.create(chatRoomRequest.getRoomName());
        return "redirect:/chat/home";
    }

    @Getter
    private static class ChatRoomRequest {

        private final String roomName;

        public ChatRoomRequest(String roomName) {
            this.roomName = roomName;
        }
    }
}
