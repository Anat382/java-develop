package ru.otus.server.profanity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ProfanityFilterTest {

    private ProfanityFilter filter;

    @BeforeEach
    void setUp() throws Exception {
        filter = new ProfanityFilter();
        Field patternField = ProfanityFilter.class.getDeclaredField("pattern");
        patternField.setAccessible(true);
        Pattern testPattern = Pattern.compile("\\b(дурак|идиот)\\b",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        patternField.set(filter, testPattern);
        System.out.println("Тестовая конфигурация выполнена");
    }

    @Test
    void shouldFilterBadWords() {
        assertEquals("Привет, *****!", filter.filter("Привет, дурак!"));
        assertEquals("*****, ты кто?", filter.filter("идиот, ты кто?"));
        assertEquals("Нормальное слово", filter.filter("Нормальное слово"));
        System.out.println("shouldFilterBadWords пройден");
    }

    @Test
    void shouldFilterMultipleBadWords() {
        assertEquals("***** и *****!", filter.filter("дурак и идиот!"));
        System.out.println("shouldFilterMultipleBadWords пройден");
    }

    @Test
    void shouldIgnoreCase() {
        assertEquals("*****!", filter.filter("Дурак!"));
        assertEquals("*****!", filter.filter("ИДИОТ!"));
        System.out.println("shouldIgnoreCase пройден");
    }

    @Test
    void shouldNotFilterPartialWords() {
        assertEquals("дураки", filter.filter("дураки"));
        assertEquals("идиотский", filter.filter("идиотский"));
        System.out.println("shouldNotFilterPartialWords пройден");
    }
}