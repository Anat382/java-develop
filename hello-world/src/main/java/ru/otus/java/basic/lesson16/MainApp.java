package ru.otus.java.basic.lesson16;

import java.util.*;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Список из массива мин и макс: " + paramList(2, 15));

        List<Integer> values = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
        System.out.println("Сумма значений больше 5: " + sumValueList(values));

        System.out.println("Замена значений: " + setValueList(0, values));

        System.out.println("Увеличение значений на заданное число: " + increcaseValueList(2, paramList(2, 15)));

        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee("Oleg", 22));
        employees.add(new Employee("Ivan", 25));
        employees.add(new Employee("Elena", 28));
        employees.add(new Employee("Artem", 18));
        employees.add(new Employee("Danil", 35));
        employees.add(new Employee("Egor", 40));
        employees.add(new Employee("Dima", 55));

        System.out.println("Список имён сотрудников: " + EmployeeUtils.getNames(employees));

        int age = 30;
        System.out.println("Список имён сотрудников старше " + age + ": " + EmployeeUtils.getNamesByMinAge(employees, age));

        System.out.println("Проверка среднего возраста: ");
        EmployeeUtils.checkAvgAge(employees, 25);

        int youngIndex = EmployeeUtils.youngEmployeeIndex(employees);
        Employee youngest = employees.get(youngIndex);
        System.out.println("Минимальный возраст сотрудника: " + youngest.getAge());
        System.out.println("Имя самого молодого сотрудника: " + youngest.getName());

    }

    public static List<Integer> paramList(int min, int max) {
        List<Integer> values = new ArrayList<>();
        for (int i = min; i < max; i++) {
            values.add(i);
        }
        return values;
    }

    public static int sumValueList(List<Integer> valueList) {
        int value = 0;
        for (Integer val : valueList) {
            if (val > 5) {
                value += val;
            }
        }
        return value;
    }

    public static List<Integer> setValueList(int value, List<Integer> valueList) {
        for (int i = 0; i < valueList.size(); i++) {
            valueList.set(i, value);
        }
        return valueList;
    }

    public static List<Integer> increcaseValueList(int value, List<Integer> valueList) {
        for (int i = 0; i < valueList.size(); i++) {
            valueList.set(i, valueList.get(i) + value);
        }
        return valueList;
    }

}
