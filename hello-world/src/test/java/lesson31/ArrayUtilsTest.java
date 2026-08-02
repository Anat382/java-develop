package lesson31;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.java.basic.lesson31.ArrayUtils;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilsTest {


    @ParameterizedTest(name = "массив {0} -> {1}")
    @MethodSource("provideArraysForIsOnlyOneAndTwo")
    @DisplayName("Проверка, что массив состоит только из 1 и 2 и содержит оба числа")
    void isOnlyOneAndTwo(int[] array, boolean expected) {
        assertEquals(expected, ArrayUtils.isOnlyOneAndTwo(array));
    }

    static Stream<Arguments> provideArraysForIsOnlyOneAndTwo() {
        return Stream.of(
                Arguments.arguments(new int[]{1, 2}, true),
                Arguments.arguments(new int[]{1, 1}, false),
                Arguments.arguments(new int[]{1, 3}, false),
                Arguments.arguments(new int[]{1, 2, 2, 1}, true),
                Arguments.arguments(new int[]{2, 2, 2}, false),
                Arguments.arguments(new int[]{1, 1, 1}, false),
                Arguments.arguments(new int[]{}, false),
                Arguments.arguments(new int[]{1, 2, 3, 4}, false),
                Arguments.arguments(new int[]{0, 1, 2}, false),
                Arguments.arguments(new int[]{1, 2, 1, 2, 2}, true)
        );
    }

    @Test
    @DisplayName("Типичный случай: после последней единицы есть элементы")
    void getAfterLastOne_typicalCase() {
        int[] input = {1, 2, 1, 2, 2};
        int[] expected = {2, 2};
        assertArrayEquals(expected, ArrayUtils.getAfterLastOne(input));
    }

    @Test
    @DisplayName("Единица в конце — возвращаем пустой массив")
    void getAfterLastOne_oneAtEnd() {
        int[] input = {2, 2, 2, 1};
        int[] expected = {};
        assertArrayEquals(expected, ArrayUtils.getAfterLastOne(input));
    }

    @Test
    @DisplayName("Единица в середине, после неё другие числа")
    void getAfterLastOne_oneInMiddle() {
        int[] input = {1, 2, 3, 4};
        int[] expected = {2, 3, 4};
        assertArrayEquals(expected, ArrayUtils.getAfterLastOne(input));
    }

    @Test
    @DisplayName("Массив не содержит единиц — исключение")
    void getAfterLastOne_noOnes_throwsException() {
        int[] input = {2, 2, 2, 2};
        assertThrows(RuntimeException.class, () -> ArrayUtils.getAfterLastOne(input));
    }

    @Test
    @DisplayName("Пустой массив — исключение")
    void getAfterLastOne_emptyArray_throwsException() {
        int[] input = {};
        assertThrows(RuntimeException.class, () -> ArrayUtils.getAfterLastOne(input));
    }

    @Test
    @DisplayName("Массив содержит только единицу — возвращаем пустой массив")
    void getAfterLastOne_onlyOne() {
        int[] input = {1};
        int[] expected = {};
        assertArrayEquals(expected, ArrayUtils.getAfterLastOne(input));
    }

    @Test
    @DisplayName("Несколько единиц, последняя в середине")
    void getAfterLastOne_multipleOnes() {
        int[] input = {1, 2, 1, 3, 1, 4, 5};
        int[] expected = {4, 5};
        assertArrayEquals(expected, ArrayUtils.getAfterLastOne(input));
    }
}