package com.example.webchat.chat.room.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.webchat.chat.room.ChatRoom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @Test
    void testCreate() {
        String roomName = "room1";
        ChatRoom chatRoom = chatRoomService.create(roomName);

        assertThat(chatRoom.getName()).isEqualTo(roomName);
    }

    @Test
    void testFindAll() {
        chatRoomService.create("room1");
        chatRoomService.create("room2");

        assertThat(chatRoomService.findAll().size()).isEqualTo(2);
    }

    @Test
    void testFindByRoomId() {
        ChatRoom chatRoom = chatRoomService.create("room1");

        assertThat(chatRoom.getName()).isEqualTo(chatRoomService.findByRoomId(chatRoom.getId()).getName());
    }

    @Test
    void testFindRoomInfos() {
        chatRoomService.create("room1");

        assertThat(chatRoomService.findRoomInfos().size()).isEqualTo(1);
        assertThat(chatRoomService.findRoomInfos().get(0).getUserCount()).isEqualTo(0);
    }

    @Test
    void testAddUser() {
        String userName = "user1";
        ChatRoom chatRoom = chatRoomService.create("room1");
        chatRoomService.addUser(chatRoom.getId(), userName);

        assertThat(chatRoomService.getRoomUsers(chatRoom.getId()).size()).isEqualTo(1);
        assertThat(chatRoomService.getRoomUsers(chatRoom.getId())).contains(userName);
    }

}