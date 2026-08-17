package ru.otus.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServerTest {

    @Mock
    private ClientHandler mockClient1;
    @Mock
    private ClientHandler mockClient2;
    @Mock
    private ru.otus.server.authentication.AuthenticatedProvider mockAuthProvider;
    @Mock
    private ru.otus.server.room.IRoomManager mockRoomManager;
    @Mock
    private ru.otus.server.ban.IBanManager mockBanManager;
    @Mock
    private ru.otus.server.spam.ISpamFilter mockSpamFilter;
    @Mock
    private ru.otus.server.profanity.IProfanityFilter mockProfanityFilter;

    private Server server;

    @BeforeEach
    void setUp() {
        server = new Server(8189, mockAuthProvider, mockRoomManager, mockBanManager,
                mockSpamFilter, mockProfanityFilter);
    }

    @Test
    void testSubscribe() {
        when(mockClient1.getUsername()).thenReturn("user1");
        when(mockClient1.isAuthenticated()).thenReturn(true);
        when(mockRoomManager.getRoomListForClient()).thenReturn("rooms");
        doNothing().when(mockClient1).sendMessage(anyString());

        server.subscribe(mockClient1);

        assertTrue(server.getClients().contains(mockClient1));
        verify(mockClient1, times(3)).sendMessage(anyString());
    }

    @Test
    void testBroadcastMessage() {
        // Добавляем клиентов в список вручную – стабы не нужны
        server.getClients().add(mockClient1);
        server.getClients().add(mockClient2);

        server.broadcastMessage("admin", "Hello");

        verify(mockClient1).sendMessage(argThat(s -> s.contains("admin") && s.contains("Hello")));
        verify(mockClient2).sendMessage(argThat(s -> s.contains("admin") && s.contains("Hello")));
    }

    @Test
    void testUnsubscribe() {
        when(mockClient1.getUsername()).thenReturn("user1");
        server.getClients().add(mockClient1);

        server.unsubscribe(mockClient1, true);

        assertFalse(server.getClients().contains(mockClient1));
        verify(mockRoomManager).leaveRoom(mockClient1);
        verify(mockAuthProvider).updateLastActivity("user1");
    }
}