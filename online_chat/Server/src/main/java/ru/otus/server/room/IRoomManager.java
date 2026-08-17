package ru.otus.server.room;

import ru.otus.server.ClientHandler;

public interface IRoomManager {
    boolean createRoom(String name, String ownerUsername, String password);
    boolean joinRoom(String roomName, ClientHandler client, String password);
    void leaveRoom(ClientHandler client);
    void sendMessageToRoom(String roomName, String sender, String message);
    String getRoomListForClient();
    String getRoomInfo(String roomName);
    void cleanupOldRooms();
    void updateNickname(String oldNick, String newNick);
}