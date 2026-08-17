package ru.otus.server;

import ru.otus.server.authentication.AuthenticatedProvider;
import ru.otus.server.authentication.DatabaseAuthenticatedProvider;
import ru.otus.server.ban.BanManager;
import ru.otus.server.ban.IBanManager;
import ru.otus.server.profanity.IProfanityFilter;
import ru.otus.server.profanity.ProfanityFilter;
import ru.otus.server.room.IRoomManager;
import ru.otus.server.room.RoomManager;
import ru.otus.server.spam.ISpamFilter;
import ru.otus.server.spam.SpamFilter;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    private final int port;
    private final List<ClientHandler> clients;
    private final AuthenticatedProvider authenticatedProvider;
    private final IRoomManager roomManager;
    private final IBanManager banManager;
    private final ISpamFilter spamFilter;
    private final IProfanityFilter profanityFilter;
    private final ScheduledExecutorService scheduler;


    public Server(int port, AuthenticatedProvider authProvider,
                  IRoomManager roomManager, IBanManager banManager,
                  ISpamFilter spamFilter, IProfanityFilter profanityFilter) {
        this.port = port;
        this.clients = new CopyOnWriteArrayList<>();
        this.authenticatedProvider = authProvider;
        this.roomManager = roomManager;
        this.banManager = banManager;
        this.spamFilter = spamFilter;
        this.profanityFilter = profanityFilter;
        this.scheduler = Executors.newScheduledThreadPool(2);
        authenticatedProvider.init();
        startCleanupTasks();
    }


    public Server(int port) {
        this.port = port;
        this.clients = new CopyOnWriteArrayList<>();
        this.authenticatedProvider = new DatabaseAuthenticatedProvider(this);
        this.roomManager = new RoomManager(this);
        this.banManager = new BanManager();          // <-- без параметров
        this.spamFilter = new SpamFilter(5, 30);
        this.profanityFilter = new ProfanityFilter();
        this.scheduler = Executors.newScheduledThreadPool(2);
        authenticatedProvider.init();
        startCleanupTasks();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private void startCleanupTasks() {
        scheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            for (ClientHandler ch : clients) {
                if (ch.isAuthenticated() && (now - ch.getLastActivityTime() > 20 * 60 * 1000)) {
                    ch.sendMessage("/kickok Вы были отключены за неактивность (20 минут)");
                    ch.disconnect();
                }
            }
        }, 1, 1, TimeUnit.MINUTES);

        scheduler.scheduleAtFixedRate(() -> {
            roomManager.cleanupOldRooms();
        }, 1, 1, TimeUnit.DAYS);
    }

    public void shutdown() {
        System.out.println("Сервер останавливается...");
        for (ClientHandler ch : clients) {
            ch.sendMessage("Сервер остановлен администратором");
            ch.disconnect();
        }
        scheduler.shutdownNow();
        System.exit(0);
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
        broadcastMessage("admin", "Клиент " + client.getUsername() + " вошёл в чат");
        client.sendMessage("/roomlist " + roomManager.getRoomListForClient());
        sendActiveList(client);
    }

    public void unsubscribe(ClientHandler client, boolean sendExitMessage) {
        if (client.getUsername() != null) {
            if (sendExitMessage) {
                broadcastMessage("admin", "Клиент " + client.getUsername() + " покинул чат");
            }
            roomManager.leaveRoom(client);
            authenticatedProvider.updateLastActivity(client.getUsername());
        }
        clients.remove(client);
    }

    public void broadcastMessage(String sender, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String msg = String.format("[%s] %s%s:%s %s", timestamp,
                ConsoleColors.CYAN_BOLD, sender, ConsoleColors.RESET, message);
        for (ClientHandler c : clients) {
            c.sendMessage(msg);
        }
    }

    public void sendPrivateMessage(String sender, String targetUsername, String message) {
        ClientHandler target = getClientByUsername(targetUsername);
        if (target == null) {
            ClientHandler senderHandler = getClientByUsername(sender);
            if (senderHandler != null) {
                senderHandler.sendMessage("Пользователь " + targetUsername + " не найден");
            }
            return;
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String formatted = String.format("[%s] [ЛС от %s]: %s", timestamp, sender, message);
        target.sendMessage(formatted);
        ClientHandler senderHandler = getClientByUsername(sender);
        if (senderHandler != null) {
            senderHandler.sendMessage(String.format("[%s] [ЛС для %s]: %s", timestamp, targetUsername, message));
        }
    }

    public boolean isUsernameBusy(String username) {
        for (ClientHandler c : clients) {
            if (c.getUsername() != null && c.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public ClientHandler getClientByUsername(String username) {
        for (ClientHandler c : clients) {
            if (c.getUsername() != null && c.getUsername().equals(username)) {
                return c;
            }
        }
        return null;
    }

    public void sendActiveList(ClientHandler target) {
        StringBuilder sb = new StringBuilder("Активные пользователи: ");
        for (ClientHandler c : clients) {
            if (c.isAuthenticated()) {
                sb.append(c.getUsername()).append(" ");
            }
        }
        target.sendMessage(sb.toString());
    }

    // ----- Геттеры для менеджеров -----
    public AuthenticatedProvider getAuthenticatedProvider() {
        return authenticatedProvider;
    }

    public IRoomManager getRoomManager() {
        return roomManager;
    }

    public IBanManager getBanManager() {
        return banManager;
    }

    public ISpamFilter getSpamFilter() {
        return spamFilter;
    }

    public IProfanityFilter getProfanityFilter() {
        return profanityFilter;
    }

    public List<ClientHandler> getClients() {
        return clients;
    }
}