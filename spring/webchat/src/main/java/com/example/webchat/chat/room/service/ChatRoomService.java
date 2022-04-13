package com.example.webchat.chat.room.service;

import com.example.webchat.chat.room.ChatRoom;
import com.example.webchat.chat.room.ChatRoomConst;
import com.example.webchat.chat.room.dto.ChatRoomResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    private final HashOperations<String, String, ChatRoom> rooms;
    private final SetOperations<String, Object> users;

    public ChatRoomService(RedisTemplate<String, Object> redisTemplate) {
        this.rooms = redisTemplate.opsForHash();
        this.users = redisTemplate.opsForSet();
    }

    public ChatRoom create(String roomName) {
        ChatRoom chatRoom = new ChatRoom(roomName);
        rooms.put(ChatRoomConst.CHATROOM_OPS_ID, chatRoom.getId(), chatRoom);
        return chatRoom;
    }

    public List<ChatRoom> findAll() {
        return rooms.values(ChatRoomConst.CHATROOM_OPS_ID);
    }

    public ChatRoom findByRoomId(String roomId) {
        return rooms.get(ChatRoomConst.CHATROOM_OPS_ID, roomId);
    }

    public List<ChatRoomResponse> findRoomInfos() {
        return findAll()
            .stream()
            .map(chatRoom -> {
                return new ChatRoomResponse(
                    chatRoom,
                    Optional.ofNullable(getRoomUsers(chatRoom.getId())).orElseGet(HashSet::new)
                        .size());
            })
            .collect(Collectors.toList());
    }

    public Set<Object> getRoomUsers(String roomId) {
        return users.members(ChatRoomConst.USER_OPS_ID + roomId);
    }

    public boolean isAlreadyEnter(String roomId, String userName) {
        return Optional.ofNullable(users.members(ChatRoomConst.USER_OPS_ID + roomId))
            .orElseGet(HashSet::new)
            .contains(userName);
    }

    public void addUser(String roomId, String userName) {
        ChatRoom byRoomId;
        if ((byRoomId = findByRoomId(roomId)) == null) {
            return;
        }
        users.add(ChatRoomConst.USER_OPS_ID + byRoomId.getId(), userName);
    }
}