package ru.otus.server.spam;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SpamFilter implements ISpamFilter {
    private final int maxMessages;
    private final long intervalMillis;
    private final Map<String, UserMessageInfo> users = new ConcurrentHashMap<>();

    public SpamFilter(int maxMessages, int intervalSeconds) {
        this.maxMessages = maxMessages;
        this.intervalMillis = intervalSeconds * 1000L;
    }

    public SpamFilter() {
        this(5, 30);
    }

    @Override
    public boolean checkAndUpdate(String username) {
        UserMessageInfo info = users.computeIfAbsent(username, k -> new UserMessageInfo());
        long now = System.currentTimeMillis();

        synchronized (info) {
            if (now - info.lastResetTime > intervalMillis) {
                info.count.set(0);
                info.lastResetTime = now;
            }
            int currentCount = info.count.incrementAndGet();
            if (currentCount > maxMessages) {
                info.count.decrementAndGet();
                return false;
            }
            return true;
        }
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    private static class UserMessageInfo {
        private final AtomicInteger count = new AtomicInteger(0);
        private long lastResetTime = System.currentTimeMillis();
    }
}