package ru.otus.server.spam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpamFilterTest {

    private SpamFilter spamFilter;

    @BeforeEach
    void setUp() {
        spamFilter = new SpamFilter(3, 1);
    }

    @Test
    void shouldAllowMessagesWithinLimit() {
        assertTrue(spamFilter.checkAndUpdate("user1"));
        assertTrue(spamFilter.checkAndUpdate("user1"));
        assertTrue(spamFilter.checkAndUpdate("user1"));
    }

    @Test
    void shouldBlockMessagesAfterLimit() {
        assertTrue(spamFilter.checkAndUpdate("user1"));
        assertTrue(spamFilter.checkAndUpdate("user1"));
        assertTrue(spamFilter.checkAndUpdate("user1"));
        // Четвёртое сообщение должно быть заблокировано
        assertFalse(spamFilter.checkAndUpdate("user1"));
    }

    @Test
    void shouldResetCounterAfterInterval() throws InterruptedException {
        assertTrue(spamFilter.checkAndUpdate("user1"));
        assertTrue(spamFilter.checkAndUpdate("user1"));
        assertTrue(spamFilter.checkAndUpdate("user1"));
        // Ждём больше 1 секунды
        Thread.sleep(1100);
        // Счётчик сброшен, новое сообщение разрешено
        assertTrue(spamFilter.checkAndUpdate("user1"));
    }

    @Test
    void shouldMaintainSeparateCountersForDifferentUsers() {
        assertTrue(spamFilter.checkAndUpdate("user1"));
        assertTrue(spamFilter.checkAndUpdate("user1"));
        assertTrue(spamFilter.checkAndUpdate("user1"));
        assertFalse(spamFilter.checkAndUpdate("user1"));

        // user2 ещё не отправлял, должно быть разрешено
        assertTrue(spamFilter.checkAndUpdate("user2"));
        assertTrue(spamFilter.checkAndUpdate("user2"));
        assertTrue(spamFilter.checkAndUpdate("user2"));
        assertFalse(spamFilter.checkAndUpdate("user2"));
    }
}