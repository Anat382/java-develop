package ru.otus.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Server {
    private int port;
    private List<ClientHandler> clients;
    private AuthenticatedProvider authenticatedProvider;

    public Server(int port) {
        this.port = port;
        this.clients = new CopyOnWriteArrayList<ClientHandler>();
        this.authenticatedProvider = new InMemoryAuthenticatedProvider(this);
        authenticatedProvider.init();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started...");
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket, this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
    }

    public void unsubscribe(ClientHandler client, boolean sendExitMessage) {
        if (sendExitMessage) {
            broadcastMessage("admin", String.format("Client %s exit", client.getUsername()));
        }
        clients.remove(client);
    }

    public void broadcastMessage(String sender, String message) {
        String msg = String.format("%s%s:%s %s", ConsoleColors.CYAN_BOLD, sender, ConsoleColors.RESET, message);

        for (ClientHandler c : clients) {
            c.sendMessage(msg);
        }
    }

    public boolean isUsernameBusy(String username) {
        for (ClientHandler c : clients) {
            if (c.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public AuthenticatedProvider getAuthenticatedProvider() {
        return authenticatedProvider;
    }

    public ClientHandler getClientByUsername(String username) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(username)) {
                return client;
            }
        }
        return null;
    }

}
