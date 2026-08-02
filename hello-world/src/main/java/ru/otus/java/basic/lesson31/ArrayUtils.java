package ru.otus.java.basic.lesson31;

import java.util.Arrays;

public class ArrayUtils {

    public static int[] getAfterLastOne(int[] arr) {
        int lastOneIndex = -1;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 1) {
                lastOneIndex = i;
                break;
            }
        }
        if (lastOneIndex == -1) {
            throw new RuntimeException("Массив не содержит единиц");
        }
        return Arrays.copyOfRange(arr, lastOneIndex + 1, arr.length);
    }

    public static boolean isOnlyOneAndTwo(int[] arr) {
        boolean hasOne = false;
        boolean hasTwo = false;

        for (int value : arr) {
            if (value == 1) {
                hasOne = true;
            } else if (value == 2) {
                hasTwo = true;
            } else {
                return false;
            }
        }
        return hasOne && hasTwo;
    }
}