package ru.otus.java.basic.lesson16;

import java.util.*;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Список из массива мин и макс: " + paramList(2, 15));

        List<Integer> values = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
        System.out.println("Сумма значений больше 5: " + sumValueList(values));

        System.out.println("Замена значений: " + setValueList(0, values));

        System.out.println("Увеличение значений на заданное число: " + increcaseValueList(2, paramList(2, 15)));

        Employee employee = new Employee();
        employee.addEmployee(new Employee("Oleg", 22));
        employee.addEmployee(new Employee("Ivan", 25));
        employee.addEmployee(new Employee("Elena", 28));
        employee.addEmployee(new Employee("Artem", 18));
        employee.addEmployee(new Employee("Danil", 35));
        employee.addEmployee(new Employee("Egor", 40));
        employee.addEmployee(new Employee("Dima", 55));

        List<Employee> employees = new ArrayList<>(employee.getEmployee());

        System.out.println("\nСписок имён сотрудников: " + employee.getNames(employees));
        int age = 30;
        System.out.println("Список имён сотрудников старше " + age + ": " + employee.getNamesAge(employees, age));
        System.out.println("Проверка среднего возраста: ");
        employee.checkAvgAge(employees, 25);

        System.out.println("Минимальный  возраст сотрудника: " + employees.get(employee.youngEmployeeIndex(employees)).getAge());
        System.out.println("Имя самого молодого сотрудника: " + employees.get(employee.youngEmployeeIndex(employees)).getName());

    }

    public static List<Integer> paramList(int min, int max) {
        List<Integer> values = new ArrayList<>();
        for (int i = min; i < max; i++) {
            values.add(i);
        }
        return values;
    }

    public static int sumValueList(List<Integer> ArrayList) {
        int value = 0;
        for (Integer val : ArrayList) {
            if (val > 5) {
                value += val;
            }
        }
        return value;
    }

    public static List<Integer> setValueList(int value, List<Integer> ArrayList) {
        for (int i = 0; i < ArrayList.size(); i++) {
            ArrayList.set(i, value);
        }
        return ArrayList;
    }

    public static List<Integer> increcaseValueList(int value, List<Integer> ArrayList) {
        for (int i = 0; i < ArrayList.size(); i++) {
            ArrayList.set(i, ArrayList.get(i) + value);
        }
        return ArrayList;
    }

}
