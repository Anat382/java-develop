package ru.otus.java.basic.lesson20;

public class CalculateUtils {

    public double parseAndCalculate(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Выражение не может быть пустым");
        }

        String cleaned = expression.replaceAll("\\s+", "");

        if (cleaned.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Выражение не прошло валидацию: " + expression);
        }

        int operatorIndex = -1;
        char operator = 0;
        for (int i = 0; i < cleaned.length(); i++) {
            char c = cleaned.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (operatorIndex != -1) {
                    throw new IllegalArgumentException("Выражение не соответствует формату(пример n+n) : " + expression);
                }
                operatorIndex = i;
                operator = c;
            }
        }

        if (operatorIndex == -1) {
            throw new IllegalArgumentException("Нет нужного оператора допустимы /*+- : " + expression);
        }

        String leftStr = cleaned.substring(0, operatorIndex);
        String rightStr = cleaned.substring(operatorIndex + 1);

        if (leftStr.isEmpty() || rightStr.isEmpty()) {
            throw new IllegalArgumentException("Не указаны значения: " + expression);
        }

        double left, right;
        try {
            left = Double.parseDouble(leftStr);
            right = Double.parseDouble(rightStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка преданного формата для вычисления: " + expression, e);
        }

        double result;
        if (operator == '+') {
            result = left + right;
        } else if (operator == '-') {
            result = left - right;
        } else if (operator == '*') {
            result = left * right;
        } else if (operator == '/') {
            if (right == 0) {
                throw new IllegalArgumentException("Запрещено деление на 0: " + expression);
            }
            result = left / right;
        } else {
            throw new IllegalArgumentException("Операция не может быть выполнена, проверьте выражение: " + expression);
        }

        return result;
    }
}
