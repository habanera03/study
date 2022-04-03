package com.example.webchat.chat.room.service;

import com.example.webchat.chat.room.ChatRoom;
import com.example.webchat.chat.room.dto.ChatRoomResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    private static final String CHATROOM_OPS_ID = "room";
    private static final String USER_OPS_ID = "user";

    private final HashOperations<String, String, ChatRoom> rooms;
    private final SetOperations<String, Object> users;

    public ChatRoomService(RedisTemplate<String, Object> redisTemplate) {
        this.rooms = redisTemplate.opsForHash();
        this.users = redisTemplate.opsForSet();
    }

    public ChatRoom create(String roomName) {
        ChatRoom chatRoom = new ChatRoom(roomName);
        rooms.put(CHATROOM_OPS_ID, chatRoom.getId(), chatRoom);
        return chatRoom;
    }

    public List<ChatRoom> findAll() {
        return rooms.values(CHATROOM_OPS_ID);
    }

    public ChatRoom findByRoomId(String roomId) {
        return rooms.get(CHATROOM_OPS_ID, roomId);
    }

    public List<ChatRoomResponse> findRoomInfos() {
        return findAll()
            .stream()
            .map(chatRoom -> new ChatRoomResponse(
                chatRoom,
                Optional.ofNullable(users.members(USER_OPS_ID + chatRoom.getId())).orElseGet(HashSet::new).size()))
            .collect(Collectors.toList());
    }
}